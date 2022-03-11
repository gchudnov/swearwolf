package com.github.gchudnov.swearwolf.util.colors

import com.github.gchudnov.swearwolf.util.colors.AnyColor
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.EitherMonad

final case class EitherColor(r: Byte, g: Byte, b: Byte) extends Color

object EitherColor extends AnyColor[Either[Throwable, *], EitherColor]:

  override def make(r: Byte, g: Byte, b: Byte): EitherColor =
    new EitherColor(r, g, b)
