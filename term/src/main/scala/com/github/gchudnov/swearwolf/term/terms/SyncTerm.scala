package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.terms.AnyTerm
import com.github.gchudnov.swearwolf.term.terms.IdSyncTerm.make
import com.github.gchudnov.swearwolf.util.func.MonadError

import java.io.{ BufferedOutputStream, InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter

/**
 * Synchronous Terminal
 */
abstract class SyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(using ME: MonadError[F]) extends AnyTerm[F](in, out, isClose) {}

abstract class SyncTermFactory[F[_]: MonadError]:

  def make(in: InputStream, out: OutputStream): SyncTerm[F]

  def fileLog(
    path: Path,
    term: Term[F] = make(System.in, System.out),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter
  ): Term[F] =
    LogTerm.fileLog(term, path, isTruncate, fmt)
