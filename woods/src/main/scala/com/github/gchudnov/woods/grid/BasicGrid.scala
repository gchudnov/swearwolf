package com.github.gchudnov.woods.grid

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.{Grid, GridStyle}

final case class BasicGrid(size: Size, cell: Size, style: GridStyle) extends Grid
