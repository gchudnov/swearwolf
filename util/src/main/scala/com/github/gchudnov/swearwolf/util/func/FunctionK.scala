package com.github.gchudnov.swearwolf.util.func

trait FunctionK[F[_], G[_]]:
  def apply[A](fa: F[A]): G[A]
