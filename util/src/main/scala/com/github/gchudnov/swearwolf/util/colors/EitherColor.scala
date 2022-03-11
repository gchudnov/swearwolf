package com.github.gchudnov.swearwolf.util.colors

import com.github.gchudnov.swearwolf.util.colors.AnyColor
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.EitherMonad

object EitherColor extends AnyColor[Either[Throwable, *]]
