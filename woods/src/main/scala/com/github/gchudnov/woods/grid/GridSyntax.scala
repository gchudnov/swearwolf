package com.github.gchudnov.woods.grid

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point, TextStyle }
import com.github.gchudnov.woods.Grid
import com.github.gchudnov.woods.grid.impl.GridDrawer

private[grid] class GridOps(private val screen: Screen) extends AnyVal {
  def put(pt: Point, grid: Grid, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
    GridDrawer.draw(screen)(pt, grid, textStyle)
}

private[woods] trait GridSyntax {
  implicit def gridOps(screen: Screen): GridOps = new GridOps(screen)
}

object GridSyntax extends GridSyntax
