package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.shapes.grid.Grid
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyGrid[F[_]: MonadError]:

  extension (screen: Screen[F])
    def putGrid(pt: Point, grid: Grid, textStyle: TextStyle): F[Unit] =
      Grid.put(screen, pt, grid, textStyle)

    def putGrid(pt: Point, grid: Grid): F[Unit] =
      putGrid(pt, grid, TextStyle.Empty)
