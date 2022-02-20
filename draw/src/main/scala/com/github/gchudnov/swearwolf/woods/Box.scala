package com.github.gchudnov.swearwolf.draw

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.draw.box.BasicBox

trait Box:
  def size: Size
  def style: BoxStyle

object Box:
  def apply(size: Size, style: BoxStyle): Box =
    BasicBox(size = size, style = style)
