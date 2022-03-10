package com.github.gchudnov.swearwolf.util.exec

import com.github.gchudnov.swearwolf.util.func.MonadError

object Exec:
  /**
   * Execute the given array of arguments.
   */
  def exec[F[_]](as: Array[String])(using ME: MonadError[F]): F[Unit] =
    ME.attempt(Runtime.getRuntime.exec(as))
