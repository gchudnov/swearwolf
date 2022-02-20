package com.github.gchudnov.swearwolf.draw.grid

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.draw.{ Grid, GridStyle }

private[draw] final case class BasicGrid(size: Size, cell: Size, style: GridStyle) extends Grid
