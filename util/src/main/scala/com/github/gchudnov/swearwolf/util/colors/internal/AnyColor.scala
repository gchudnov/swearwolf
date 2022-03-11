package com.github.gchudnov.swearwolf.util.colors.internal

import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.colors.Color

abstract class AnyColor[F[_]: MonadError]:

  def parse(value: String): F[Color] =
    Color.parse[F](value)

  private def fromName(name: String): F[Color] =
    Color.fromName[F](name)

  private def fromHex(value: String): F[Color] =
    Color.fromHex[F](value)
