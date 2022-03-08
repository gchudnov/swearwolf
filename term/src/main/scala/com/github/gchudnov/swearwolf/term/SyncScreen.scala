package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadError

/**
 * Sync Screen
 */
abstract class SyncScreen[F[_]](term: Term[F])(implicit val ME: MonadError[F]) extends AnyScreen[F](term)(ME) {}
