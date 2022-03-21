package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.util.func.IdMonad
import com.github.gchudnov.swearwolf.util.func.Identity

import java.io.{ InputStream, OutputStream }

final class IdSyncTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends SyncTerm[Identity](in = in, out = out, isClose = isClose)

object IdSyncTerm:

  def make(in: InputStream = System.in, out: OutputStream = System.out): IdSyncTerm =
    new IdSyncTerm(in = in, out = out, isClose = false)
