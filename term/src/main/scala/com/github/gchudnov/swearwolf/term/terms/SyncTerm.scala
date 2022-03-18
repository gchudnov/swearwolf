package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.terms.AnyTerm
import com.github.gchudnov.swearwolf.util.func.MonadError

import java.io.{BufferedOutputStream, InputStream, OutputStream}

/**
 * Synchronous Terminal
 */
abstract class SyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(using ME: MonadError[F]) extends AnyTerm[F](in, out, isClose) {}
