package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.util.func.EitherMonad

import java.io.{ InputStream, OutputStream }

final class EitherSyncTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends SyncTerm[Either[Throwable, *]](in = in, out = out, isClose = isClose)

object EitherSyncTerm:

  def make(in: InputStream = System.in, out: OutputStream = System.out): EitherSyncTerm =
    new EitherSyncTerm(in = in, out = out, isClose = false)
