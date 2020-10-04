package com.github.gchudnov.woods

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point, TextStyle }
import com.github.gchudnov.woods.grid.impl.GridDrawer

package object grid {

  class GridOps(private val screen: Screen) extends AnyVal {
    def put(pt: Point, grid: Grid, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
      GridDrawer.draw(screen)(pt, grid, textStyle)
  }

}
