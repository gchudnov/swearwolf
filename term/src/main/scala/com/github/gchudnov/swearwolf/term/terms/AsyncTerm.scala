package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.terms.AnyTerm
import com.github.gchudnov.swearwolf.util.func.{ MonadAsyncError, MonadError }

import java.io.{ BufferedOutputStream, InputStream, OutputStream }

/**
 * Base Asynchronous Terminal
 */
abstract class AsyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(using ME: MonadAsyncError[F])
    extends AnyTerm[F](in, out, isClose) {}
