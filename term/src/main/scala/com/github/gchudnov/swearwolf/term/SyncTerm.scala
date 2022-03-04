package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.term.keys.KeySeq


abstract class SyncTerm[F[_]](implicit val monad: MonadError[F]) extends Term[F] {
  
  override def write(bytes: Array[Byte]): F[Unit] =
    ???

  override def flush(): F[Unit] =
    ???
    // monad.eval(asyncHttpClient.flush())

  override def poll(): F[Option[List[KeySeq]]] = 
    ???
}
