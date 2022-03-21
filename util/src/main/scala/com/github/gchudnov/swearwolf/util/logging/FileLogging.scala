package com.github.gchudnov.swearwolf.util.logging

import com.github.gchudnov.swearwolf.util.func.MonadError

import java.nio.charset.StandardCharsets
import java.nio.file.{ Files, Path, Paths, StandardOpenOption }
import java.time.LocalDateTime

abstract class FileLogging[F[_]](path: Path)(using ME: MonadError[F]) extends Logging[F]:
  import MonadError.*

  override def log(msg: => String): F[Unit] =
    ME.attempt(appendToLog(msg))

  private def appendToLog(text: => String): Unit =
    val bytes = s"${text}\n".getBytes(StandardCharsets.UTF_8)
    Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
