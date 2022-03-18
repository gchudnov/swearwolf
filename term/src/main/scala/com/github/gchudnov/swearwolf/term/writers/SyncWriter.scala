package com.github.gchudnov.swearwolf.term.writers

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.writers.AnyWriter
import com.github.gchudnov.swearwolf.util.func.MonadError

/**
 * Sync Writer
 */
abstract class SyncWriter[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyWriter[F](term) {}
