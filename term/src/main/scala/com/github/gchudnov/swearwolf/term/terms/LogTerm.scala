package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.{ EscSeq, Term }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.clock.{ Clock, InstantClock }
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.logging.{ FileLogging, Logging }

import java.io.{ FileOutputStream, OutputStream, OutputStreamWriter, PrintWriter }
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Path
import java.time.format.DateTimeFormatter
import java.time.{ LocalDateTime, ZoneId, ZoneOffset }

final class LogTerm[F[_]](fmt: DateTimeFormatter, logging: Logging[F], clock: Clock[F], term: Term[F])(using ME: MonadError[F])
    extends Term[F]:
  import MonadError.*

  def read(): F[Option[Array[Byte]]] =
    val msg = s"read()"
    tryLog1(msg, term.read())

  def write(bytes: Array[Byte]): F[Unit] =
    val data = Bytes(bytes)
    val msg  = s"write(${data.show})"
    tryLog0(msg, term.write(bytes))

  def flush(): F[Unit] =
    val msg = s"flush()"
    tryLog0(msg, term.flush())

  def close(): F[Unit] =
    val msg = s"close()"
    tryLog0(msg, term.flush())

  private def tryLog0(msg: => String, f: => F[Unit]): F[Unit] =
    clock
      .time()
      .flatMap(i =>
        val dt = fmt.format(i)
        f.flatMap(_ => logging.log(s"${dt}: ${msg} ...OK")).handleErrorWith(t => logging.log(s"${msg} ...ERR: '${t.getMessage}'"))
      )

  private def tryLog1(msg: => String, f: => F[Option[Array[Byte]]]): F[Option[Array[Byte]]] =
    clock
      .time()
      .flatMap(i =>
        val dt = fmt.format(i)
        f
          .flatMap((r: Option[Array[Byte]]) => logging.log(s"${dt}: ${msg} ...OK: ${r.map(Bytes(_).show)}").map(_ => r))
          .handleErrorWith(t => logging.log(s"${msg} ...ERR: '${t.getMessage}'").flatMap(_ => ME.fail(t)))
      )

object LogTerm:
  val defaultDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC))

  def fileLog[F[_]: MonadError](term: Term[F], path: Path, isTruncate: Boolean, fmt: DateTimeFormatter): Term[F] =
    if isTruncate then FileLogging.truncateFile(path)

    val logging = new FileLogging[F](path)
    val clock   = new InstantClock[F]()
    val logTerm = new LogTerm[F](fmt, logging, clock, term)
    logTerm
