package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadError

/**
 * Sync Writer
 */
abstract class SyncWriter[F[_]](term: Term[F])(implicit val ME: MonadError[F]) extends AnyWriter[F](term)(ME) {}
