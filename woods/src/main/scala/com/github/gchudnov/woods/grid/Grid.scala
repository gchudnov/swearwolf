package com.github.gchudnov.woods.grid

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.grid.GridStyle.GridStyle

trait Grid {
  def size: Size
  def cell: Size
  def style: GridStyle
}

object Grid {
  def apply(size: Size, cell: Size, style: GridStyle): Grid =
    BasicGrid(size = size, cell = cell, style = style)
}
