package com.github.gchudnov.swearwolf.draw

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.draw.grid.BasicGrid

trait Grid:
  def size: Size
  def cell: Size
  def style: GridStyle

object Grid:
  def apply(size: Size, cell: Size, style: GridStyle): Grid =
    BasicGrid(size = size, cell = cell, style = style)
