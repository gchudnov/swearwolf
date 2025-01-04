package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.terms.LogTerm
import com.github.gchudnov.swearwolf.util.func.FutureMonad

import java.io.{ InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

final class FutureAsyncTerm(in: InputStream, out: OutputStream, isClose: Boolean)
    extends AsyncTerm[Future](in = in, out = out, isClose = isClose)

object FutureSyncTerm extends AnyTermFactory[Future]:

  override def make(in: InputStream, out: OutputStream): FutureAsyncTerm =
    new FutureAsyncTerm(in = in, out = out, isClose = false)
