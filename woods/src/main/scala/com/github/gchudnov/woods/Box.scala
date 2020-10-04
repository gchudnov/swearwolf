package com.github.gchudnov.woods

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.box.BasicBox

trait Box {
  def size: Size
  def style: BoxStyle
}

object Box {
  def apply(size: Size, style: BoxStyle): Box =
    BasicBox(size = size, style = style)
}
