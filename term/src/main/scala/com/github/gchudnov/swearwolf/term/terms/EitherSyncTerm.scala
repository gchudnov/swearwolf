package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.terms.LogTerm
import com.github.gchudnov.swearwolf.util.func.EitherMonad

import java.io.{ InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter

final class EitherSyncTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends SyncTerm[Either[Throwable, *]](in = in, out = out, isClose = isClose)

object EitherSyncTerm extends SyncTermFactory[Either[Throwable, *]]:

  override def make(in: InputStream, out: OutputStream): EitherSyncTerm =
    new EitherSyncTerm(in = in, out = out, isClose = false)
