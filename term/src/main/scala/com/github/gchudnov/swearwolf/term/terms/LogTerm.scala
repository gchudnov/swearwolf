package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.{ EscSeq, Term }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.logging.Logging

import java.io.{ FileOutputStream, OutputStream, OutputStreamWriter, PrintWriter }
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Path
import java.time.LocalDateTime

final class LogTerm[F[_]](logging: Logging[F], term: Term[F])(using ME: MonadError[F]) extends Term[F]:
  import MonadError.*

  def read(): F[Option[Array[Byte]]] =
    val msg = s"read()"
    tryLog1(msg, term.read())

  def write(bytes: Array[Byte]): F[Unit] =
    val data = Bytes(bytes)
    val msg  = s"write('${data.show}')"
    tryLog0(msg, term.write(bytes))

  def flush(): F[Unit] =
    val msg = s"flush()"
    tryLog0(msg, term.flush())

  def close(): F[Unit] =
    val msg = s"close()"
    tryLog0(msg, term.flush())

  private def tryLog0(msg: => String, f: => F[Unit]): F[Unit] =
    val dt = LocalDateTime.now() // TODO: extract
    f.flatMap(_ => logging.log(s"${dt}: ${msg} ...OK")).handleErrorWith(t => logging.log(s"${msg} ...ERR: '${t.getMessage}'"))

  private def tryLog1(msg: => String, f: => F[Option[Array[Byte]]]): F[Option[Array[Byte]]] =
    val dt = LocalDateTime.now()
    f
      .flatMap(r => logging.log(s"${dt}: ${msg} ...OK: ${r}").map(_ => r))
      .handleErrorWith(t => logging.log(s"${msg} ...ERR: '${t.getMessage}'").flatMap(_ => ME.fail(t)))
