package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.{ IdMonad, Identity }

import java.io.{ InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter

final class IdSyncTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends SyncTerm[Identity](in = in, out = out, isClose = isClose)

object IdSyncTerm:

  def make(in: InputStream = System.in, out: OutputStream = System.out): IdSyncTerm =
    new IdSyncTerm(in = in, out = out, isClose = false)

  def fileLog(
    path: Path,
    term: Term[Identity] = make(),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter
  ): Term[Identity] =
    LogTerm.fileLog(term, path, isTruncate, fmt)
