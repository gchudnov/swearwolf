package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.SyncTerm
import com.github.gchudnov.swearwolf.util.func.EitherMonad

import java.io.InputStream
import java.io.OutputStream

final class EitherSyncTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends SyncTerm[Either[Throwable, *]](in = in, out = out, isClose = isClose)

object EitherSyncTerm:

  def make(): EitherSyncTerm =
    new EitherSyncTerm(in = System.in, out = System.out, isClose = false)
