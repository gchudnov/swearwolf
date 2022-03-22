package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.terms.LogTerm
import com.github.gchudnov.swearwolf.util.func.EitherMonad

import java.io.{ InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter

final class EitherSyncTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends SyncTerm[Either[Throwable, *]](in = in, out = out, isClose = isClose)

object EitherSyncTerm:

  def make(in: InputStream = System.in, out: OutputStream = System.out): EitherSyncTerm =
    new EitherSyncTerm(in = in, out = out, isClose = false)

  def fileLog(
    path: Path,
    term: Term[Either[Throwable, *]] = make(),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter
  ): Term[Either[Throwable, *]] =
    LogTerm.fileLog(term, path, isTruncate, fmt)
