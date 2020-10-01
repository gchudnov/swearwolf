package com.github.gchudnov.swearwolf.term

import java.io.{ InputStream, OutputStream }

import scala.collection.mutable.ListBuffer
import scala.util.control.Exception.nonFatalCatch

/**
 * Terminal with basic I/O operations.
 *
 * @param in input stream
 * @param out output stream
 */
class IOTerm(in: InputStream, out: OutputStream) extends Term {
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
      if (n != -1) {
        raw.append(n)
        poll()
      } else
        Right(List.empty[KeySeq])
    }

  override def poll(): Either[Throwable, List[KeySeq]] =
    nonFatalCatch.either {
      val nAvail = in.available()
      if (nAvail > 0) {
        val bytes = in.readNBytes(nAvail)
        raw.appendAll(bytes)
      }

      val (ks, rest) = Reader.consume(raw.toSeq)
      raw.clear()
      raw.appendAll(rest)

      ks.toList
    }
}
