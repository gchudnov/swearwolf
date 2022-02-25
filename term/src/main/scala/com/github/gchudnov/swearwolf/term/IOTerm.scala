package com.github.gchudnov.swearwolf.term

import java.io.{ InputStream, OutputStream }

import com.github.gchudnov.swearwolf.term.KeySeq

import scala.collection.mutable.ListBuffer
import scala.util.control.Exception.nonFatalCatch
import com.github.gchudnov.swearwolf.util.bytes.Bytes

/**
 * Terminal with basic I/O operations.
 *
 * @param in
 *   input stream
 * @param out
 *   output stream
 */
private[term] class IOTerm(in: InputStream, out: OutputStream) extends Term:
  private val raw = new ListBuffer[Byte]

  override def write(bytes: Array[Byte]): Either[Throwable, Unit] =
    nonFatalCatch.either(out.write(bytes))

  override def write(seq: EscSeq): Either[Throwable, Unit] =
    this.write(seq.bytes)

  override def write(str: String): Either[Throwable, Unit] =
    this.write(str.getBytes())

  override def flush(): Either[Throwable, Unit] =
    nonFatalCatch.either(out.flush())

  override def blockingPoll(): Either[Throwable, List[KeySeq]] =
    nonFatalCatch.either(in.read().toByte).flatMap { n =>
      if n != -1 then
        raw.append(n)
        poll()
      else Right(List.empty[KeySeq])
    }

  override def poll(): Either[Throwable, List[KeySeq]] =
    nonFatalCatch.either {
      val nAvail = in.available()
      if nAvail > 0 then
        val bytes = in.readNBytes(nAvail)
        raw.appendAll(bytes)

      val (ks, rest) = Reader.consume(Bytes(raw.toArray))
      raw.clear()
      raw.appendAll(rest.value)

      ks.toList
    }
