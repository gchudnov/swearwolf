package com.github.gchudnov.swearwolf.util.colors

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.EitherMonad

object EitherColor:
  extension (colorT: Color.type)
    def parseEither(value: String): Either[Throwable, Color] =
      Color.parse[Either[Throwable, *]](value)
