package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.MonadError

import java.io.{ InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter

abstract class AnyTermFactory[F[_]: MonadError]:

  def make(in: InputStream, out: OutputStream): Term[F]

  def fileLog(
    path: Path,
    term: Term[F] = make(System.in, System.out),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter,
  ): Term[F] =
    LogTerm.fileLog(term, path, isTruncate, fmt)
