package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadAsyncError

/**
 * Sync Writer
 */
abstract class AsyncWriter[F[_]](term: Term[F])(using ME: MonadAsyncError[F]) extends AnyWriter[F](term) {}
