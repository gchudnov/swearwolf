package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError

/**
 * Async Screen
 */
abstract class AsyncScreen[F[_]](term: Term[F])(using ME: MonadAsyncError[F]) extends AnyScreen[F](term) {}
