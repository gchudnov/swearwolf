package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.terms.LogTerm
import com.github.gchudnov.swearwolf.util.func.TryMonad

import java.io.{ InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter
import scala.util.Try

final class TrySyncTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends SyncTerm[Try](in = in, out = out, isClose = isClose)

object TrySyncTerm extends SyncTermFactory[Try]:

  override def make(in: InputStream, out: OutputStream): TrySyncTerm =
    new TrySyncTerm(in = in, out = out, isClose = false)
