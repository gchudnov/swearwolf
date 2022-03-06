package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.internal.terminals.IOTerm
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError
import com.github.gchudnov.swearwolf.util.func.MonadError
import java.io.InputStream
import java.io.OutputStream
import java.io.BufferedOutputStream

/**
 * Base Asynchronous Terminal
 */
abstract class AsyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(implicit val ME: MonadAsyncError[F]) extends AnyTerm[F](in, out, isClose)(ME) {}
