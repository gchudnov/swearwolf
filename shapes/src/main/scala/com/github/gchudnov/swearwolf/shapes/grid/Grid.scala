package com.github.gchudnov.swearwolf.shapes.grid

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.shapes.grid.internal.GridBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.func.MonadError

final case class Grid(size: Size, cell: Size, style: GridStyle)

object Grid:

  def build[F[_]: MonadError](grid: Grid): F[Seq[Span]] =
    given ME: MonadError[F] = summon[MonadError[F]]
    ME.pure(GridBuilder.build(grid))

  extension [F[_]: MonadError](screen: Screen[F])
    def put(pt: Point, grid: Grid, textStyle: TextStyle): F[Unit] =
      import MonadError.*
      given ME: MonadError[F] = summon[MonadError[F]]

      for
        spans <- build(grid)
        _     <- ME.sequence(spans.zipWithIndex.map { case (span, y) => screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span))) })
      yield ()

    def put(pt: Point, grid: Grid): F[Unit] =
      put(pt, grid, TextStyle.Empty)
