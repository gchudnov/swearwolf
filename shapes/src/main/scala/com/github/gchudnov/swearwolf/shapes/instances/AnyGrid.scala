package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.shapes.grid.Grid
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyGrid:

  extension [F[_]](screen: Screen[F])
    def put(pt: Point, grid: Grid, textStyle: TextStyle)(using MF: MonadError[F]): F[Unit] =
      Grid.put(screen, pt, grid, textStyle)

    def put(pt: Point, grid: Grid)(using MF: MonadError[F]): F[Unit] =
      put(pt, grid, TextStyle.Empty)
