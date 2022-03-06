package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadError

/**
 * Sync Screen
 */
abstract class SyncScreen[F[_]]()(implicit val ME: MonadError[F]) extends Screen[F] {

}
