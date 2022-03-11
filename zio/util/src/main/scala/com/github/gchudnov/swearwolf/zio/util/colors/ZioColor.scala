package com.github.gchudnov.swearwolf.zio.util.colors

import com.github.gchudnov.swearwolf.util.colors.AnyColor
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

final case class ZioColor(r: Byte, g: Byte, b: Byte) extends Color

object ZioColor extends AnyColor[Task, ZioColor]:

  override def make(r: Byte, g: Byte, b: Byte): ZioColor =
    new ZioColor(r, g, b)
