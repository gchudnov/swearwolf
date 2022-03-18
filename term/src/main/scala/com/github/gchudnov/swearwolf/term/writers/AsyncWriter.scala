package com.github.gchudnov.swearwolf.term.writers

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.writers.AnyWriter
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError

/**
 * Sync Writer
 */
abstract class AsyncWriter[F[_]](term: Term[F])(using ME: MonadAsyncError[F]) extends AnyWriter[F](term) {}
