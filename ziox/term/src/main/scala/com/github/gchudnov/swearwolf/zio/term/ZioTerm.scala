package com.github.gchudnov.swearwolf.zio.term

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.terms.LogTerm
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioTerm
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

import java.io.{ InputStream, OutputStream }
import java.lang.System
import java.nio.file.Path
import java.time.format.DateTimeFormatter

type ZioTerm = AsyncZioTerm

object ZioTerm:

  def layer(in: InputStream = System.in, out: OutputStream = System.out): ULayer[ZioTerm] =
    ZLayer.succeed(make(in, out))

  def stdIoLayer: ULayer[ZioTerm] =
    layer()

  def fileLogLayer(
    path: Path,
    term: Term[Task] = make(System.in, System.out),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter
  ): Term[Task] =
    LogTerm.fileLog(term, path, isTruncate, fmt)

  private def make(in: InputStream, out: OutputStream): AsyncZioTerm =
    new AsyncZioTerm(in, out, false)
