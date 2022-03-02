package com.github.gchudnov.swearwolf.term.internal

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.bytes.Bytes

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ConcurrentLinkedDeque
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*
import scala.util.control.Exception.nonFatalCatch

/**
 * Terminal with basic I/O operations.
 *
 * It is expected one reader and one writer, but they can be running in separate threads.
 *
 * @param in
 *   input stream
 * @param out
 *   output stream
 */
private[term] class IOTerm(in: InputStream, out: OutputStream) extends Term:

  private val q = ConcurrentLinkedDeque[Byte]

  override def write(bytes: Array[Byte]): Either[Throwable, Unit] =
    nonFatalCatch.either(out.write(bytes))

  override def write(seq: EscSeq): Either[Throwable, Unit] =
    this.write(seq.bytes)

  override def write(str: String): Either[Throwable, Unit] =
    this.write(str.getBytes())

  override def flush(): Either[Throwable, Unit] =
    nonFatalCatch.either(out.flush())

  override def blockingPoll(): Either[Throwable, Option[List[KeySeq]]] =
    val maybeBytes = nextChunk(1)
    maybeBytes match
      case Some(bytes) =>
        bytes.foreach(q.add)
        poll()
      case None =>
        Right(None)

  /**
   * Read StdIn and return the next chunk of KeySeq.
   *
   * Returns None if EOF is reached.
   */
  override def poll(): Either[Throwable, Option[List[KeySeq]]] =
    for
      nAvail <- nonFatalCatch.either(in.available())
      maybeBytes <- if nAvail > 0 then nonFatalCatch.either(nextChunk(nAvail))
                    else Right(Some(Array.emptyByteArray))

      rs <- maybeBytes match
              case Some(bytes) =>
                bytes.foreach(q.add)
                val ks = IOTerm.unsafeProcessNextChunk(q)
                Right(Some(ks): Option[List[KeySeq]])
              case None =>
                Right(None: Option[List[KeySeq]])
    yield rs

  private def nextChunk(maxLen: Int): Option[Array[Byte]] =
    val data = new Array[Byte](maxLen)
    val n    = in.read(data, 0, data.length)
    n match
      case n if n > 0 =>
        Some(data.take(n))
      case -1 =>
        None // EOF
      case 0 =>
        Some(Array.empty[Byte])
      case x =>
        sys.error(s"Unexpected number of bytes were read: $x")

object IOTerm:
  private def unsafeProcessNextChunk(q: ConcurrentLinkedDeque[Byte]): List[KeySeq] =
    def prependAll(bytes: Array[Byte]): Unit =
      bytes.reverse.foreach(q.addFirst)

    def removeAll(acc: ListBuffer[Byte]): Array[Byte] =
      val it = Option(q.pollFirst())
      it match
        case Some(b) =>
          acc += b
          removeAll(acc)
        case None =>
          acc.toArray

    // consume
    val bytes = removeAll(ListBuffer.empty[Byte])

    // parse
    val (ks, rest) = Reader.consume(Bytes(bytes))

    // prepend the rest back so it can be consumed again later when the KeySeq is full
    prependAll(rest.toArray)

    ks.toList
