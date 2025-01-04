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

type ZTerm = Term[Task]

private[term] trait ZioTerm:
  extension (termT: Term.type)

    def asyncZIO(in: InputStream = System.in, out: OutputStream = System.out): Term[Task] =
      ZioTerm.make(in, out)

    def asyncFileLogZIO(
      path: Path,
      term: Term[Task] = ZioTerm.make(System.in, System.out),
      isTruncate: Boolean = true,
      fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter,
    ): Term[Task] =
      LogTerm.fileLog(term, path, isTruncate, fmt)

    def layer(in: InputStream = System.in, out: OutputStream = System.out): ULayer[Term[Task]] =
      ZLayer.succeed(ZioTerm.make(in, out))

object ZioTerm:
  def make(in: InputStream = System.in, out: OutputStream = System.out): Term[Task] =
    new AsyncZioTerm(in, out, false)
