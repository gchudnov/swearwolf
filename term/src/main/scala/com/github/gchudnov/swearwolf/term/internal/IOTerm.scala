package com.github.gchudnov.swearwolf.term.internal

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.bytes.Bytes

import java.io.InputStream
import java.io.OutputStream
import scala.collection.mutable.ListBuffer
import scala.util.control.Exception.nonFatalCatch
import scala.jdk.CollectionConverters.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Terminal with basic I/O operations.
 *
 * @param in
 *   input stream
 * @param out
 *   output stream
 */
private[term] class IOTerm(in: InputStream, out: OutputStream) extends Term:

  private val bb = ConcurrentLinkedQueue[Byte]

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
        bb.add(n)
        poll()
      else Right(List.empty[KeySeq])
    }

  override def poll(): Either[Throwable, List[KeySeq]] =
    nonFatalCatch.either {
      val nAvail = in.available()
      if nAvail > 0 then
        val bytes = in.readNBytes(nAvail)
        bytes.foreach(bb.add)

      val (ks, rest) = Reader.consume(Bytes(bb.toArray.map(_.asInstanceOf[Byte])))
      bb.clear()
      rest.toArray.foreach(bb.add)

      ks.toList
    }
