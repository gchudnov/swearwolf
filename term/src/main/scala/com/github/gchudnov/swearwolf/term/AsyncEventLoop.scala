package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError

import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*
import com.github.gchudnov.swearwolf.util.func.Monoid
import scala.collection.mutable.ListBuffer
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.util.bytes.Bytes

abstract class AsyncEventLoop[F[_]](term: Term[F])(implicit val ME: MonadAsyncError[F]) extends EventLoop[F]:
  import AsyncEventLoop.*
  import EventLoop.*
  import KeySeqSyntax.*
  import MonadError.*

  private val byteQueue = ConcurrentLinkedDeque[Byte]
  private val keyQueue = ConcurrentLinkedQueue[KeySeq]

  /**
   * Returns the next KeySeq either from the queue or by reading from the terminal.
   * 
   * Blocks until a KeySeq is available.
   */
  protected def pollOne(): F[Option[KeySeq]] =

    @tailrec
    def iterate(): F[Option[KeySeq]] =
      readNextBytes()
        .flatMap {
          case Some(nBytes) if nBytes > 0 =>
            val nKeySeq = pumpBytesToKeySeq()
            if nKeySeq == 0 then
              iterate()
            else
              ME.pure(Some(keyQueue.poll()))
          case Some(nBytes) =>
            iterate()
          case None =>
            ME.pure(None)
        }

    if keyQueue.isEmpty then 
      iterate()
    else 
      ME.pure(Some(keyQueue.poll()))

  /**
   * Read next chunk of Bytes from the Term and insert them into the Bytes Queue.
   *
   * Blocking call.
   * 
   * returns Some(n) when n bytes were read OR None, if EOF is reached
   */
  private def readNextBytes(): F[Option[Int]] =
    for {
      maybeBytes <- term.read()
      n = maybeBytes match {
        case Some(bytes) =>
          bytes.foreach(byteQueue.add)
          Some(bytes.size)
        case None =>
          None
      }
    } yield n

  /**
   * Get from the Byte Queue, parse KeySeq and push them into the KeySeq Queue.
   * 
   * Returns the number of KeqSeq that were added to the KeySeq Queue.
   */
  private def pumpBytesToKeySeq(): Int =
    val bytes = dequeue(byteQueue)
    val (ks, rest) = Reader.parseBytes(Bytes(bytes.toArray))
    prepend(byteQueue, rest.asArray)
    append(keyQueue, ks)
    ks.size

  private def dequeue[T](q: ConcurrentLinkedDeque[T]): List[T] =
    @tailrec
    def iterate(acc: ListBuffer[T]): List[T] =
      val it = Option(q.pollFirst())
      it match
        case Some(x) =>
          acc += x
          iterate(acc)
        case None =>
          acc.toList    

    iterate(ListBuffer.empty[T])

  private def prepend[T](q: ConcurrentLinkedDeque[T], xs: Seq[T]): Unit =
    xs.reverse.foreach(q.addFirst)

  private def append[T](q: ConcurrentLinkedQueue[T], xs: Seq[T]): Unit =
    q.addAll(xs.asJava)



private[term] object AsyncEventLoop:
  import KeySeqSyntax.*
