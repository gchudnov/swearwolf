package com.github.gchudnov.swearwolf.draw.grid

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.geometry.{ Point }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.draw.Grid
import com.github.gchudnov.swearwolf.draw.grid.impl.GridDrawer

private[grid] trait GridOps:
  extension (screen: Screen)
    def put(pt: Point, grid: Grid, textStyle: TextStyle): Either[Throwable, Unit] =
      GridDrawer.draw(screen)(pt, grid, textStyle)

    def put(pt: Point, grid: Grid): Either[Throwable, Unit] =
      put(pt, grid, TextStyle.Empty)

private[draw] trait GridSyntax extends GridOps

object GridSyntax extends GridSyntax
