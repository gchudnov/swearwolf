package com.github.gchudnov.swearwolf.util.clock

import com.github.gchudnov.swearwolf.util.func.MonadError

import java.time.Instant

class InstantClock[F[_]]()(using ME: MonadError[F]) extends Clock[F]:
  override def time(): F[Instant] =
    ME.succeed(Instant.now())
