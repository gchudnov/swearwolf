package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.screens.AnyScreen
import com.github.gchudnov.swearwolf.util.func.{MonadAsyncError, MonadError}

/**
 * Async Screen
 */
abstract class AsyncScreen[F[_]](term: Term[F])(using ME: MonadAsyncError[F]) extends AnyScreen[F](term) {}
