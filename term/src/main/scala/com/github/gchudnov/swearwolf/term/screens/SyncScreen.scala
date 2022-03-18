package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.screens.AnyScreen
import com.github.gchudnov.swearwolf.util.func.MonadError

/**
 * Sync Screen
 */
abstract class SyncScreen[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyScreen[F](term) {}
