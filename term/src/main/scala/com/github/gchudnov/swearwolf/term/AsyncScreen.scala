package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError

/**
 * Async Screen
 */
abstract class AsyncScreen[F[_]]()(implicit val ME: MonadAsyncError[F]) extends Screen[F] {

}

// TODO: add AnyScreen instead of BasicScreen ??? that is generic.