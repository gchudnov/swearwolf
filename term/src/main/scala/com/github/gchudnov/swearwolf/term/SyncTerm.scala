package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadError
import java.io.InputStream
import java.io.OutputStream
import java.io.BufferedOutputStream

/**
 * Synchronous Terminal
 */
abstract class SyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(using ME: MonadError[F]) extends AnyTerm[F](in, out, isClose) {}
