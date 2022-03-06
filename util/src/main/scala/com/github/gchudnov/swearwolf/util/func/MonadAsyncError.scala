package com.github.gchudnov.swearwolf.util.func

trait MonadAsyncError[F[_]] extends MonadError[F]:
  def async[A](register: (Either[Throwable, A] => Unit) => Canceler): F[A]
