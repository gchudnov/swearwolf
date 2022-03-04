package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadAsyncError
import com.github.gchudnov.swearwolf.term.keys.KeySeq

/**
 * Base Asynchronous Terminal
 */
abstract class AsyncTerm[F[_]](implicit val monad: MonadAsyncError[F]) extends Term[F] {
  
  override def write(bytes: Array[Byte]): F[Unit] =
    ???

  override def flush(): F[Unit] =
    ???
    // monad.eval(asyncHttpClient.flush())

  override def poll(): F[Option[List[KeySeq]]] = 
    ???
}
