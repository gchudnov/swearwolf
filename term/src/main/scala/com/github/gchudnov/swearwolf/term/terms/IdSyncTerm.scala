package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.{ IdMonad, Identity }

import java.io.{ InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter

final class IdSyncTerm(in: InputStream, out: OutputStream, isClose: Boolean)
    extends SyncTerm[Identity](in = in, out = out, isClose = isClose)

object IdSyncTerm extends AnyTermFactory[Identity]:

  override def make(in: InputStream, out: OutputStream): SyncTerm[Identity] =
    new IdSyncTerm(in = in, out = out, isClose = false)
