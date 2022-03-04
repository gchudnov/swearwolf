package com.github.gchudnov.swearwolf.term.internal.terminals

import com.github.gchudnov.swearwolf.term.Term
import java.io.OutputStream
import java.io.PrintWriter
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets.UTF_8
import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import java.nio.file.Path
import java.io.FileOutputStream
import com.github.gchudnov.swearwolf.util.bytes.Bytes

final class LogTerm(output: OutputStream, inner: Term) extends Term:

  private val pw = new PrintWriter(new OutputStreamWriter(output, UTF_8))

  override def write(bytes: Array[Byte]): Either[Throwable, Unit] =
    val data = Bytes(bytes)
    pw.print(s"write(Array[Byte]): ${data.show} ...")
    withLogResult(inner.write(bytes))

  override def write(seq: EscSeq): Either[Throwable, Unit] =
    val data = Bytes(seq.value.getBytes)
    pw.print(s"write(EscSeq): ${data.show} ...")
    withLogResult(inner.write(seq))

  override def write(str: String): Either[Throwable, Unit] =
    val data = str
    pw.print(s"write(String): ${data} ...")
    withLogResult(inner.write(str))

  override def flush(): Either[Throwable, Unit] =
    pw.print("flush() ...")
    withLogResult(inner.flush())

  override def blockingPoll(): Either[Throwable, Option[List[KeySeq]]] =
    pw.print("blockingPoll() ...")
    withLogResult(inner.blockingPoll())

  override def poll(): Either[Throwable, Option[List[KeySeq]]] =
    pw.print("poll() ...")
    withLogResult(inner.poll())

  private def withLogResult[A](e: Either[Throwable, A]): Either[Throwable, A] =
    e match
      case Left(e) =>
        pw.println("ERR: " + e.getMessage)
        pw.flush()
        Left(e)
      case Right(a) =>
        pw.println("OK: " + a)
        pw.flush()
        Right(a)

object LogTerm:

  def make(path: Path, inner: Term): LogTerm =
    val outStream = new FileOutputStream(path.toFile)
    new LogTerm(outStream, inner)
