package com.github.gchudnov.swearwolf.woods.grid

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.swearwolf.woods.{ Grid, GridStyle }

private[woods] final case class BasicGrid(size: Size, cell: Size, style: GridStyle) extends Grid
