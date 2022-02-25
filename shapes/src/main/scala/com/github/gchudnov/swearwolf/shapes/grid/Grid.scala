package com.github.gchudnov.swearwolf.shapes.grid

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.shapes.grid.internal.GridBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.Transform
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle

final case class Grid(size: Size, cell: Size, style: GridStyle)

object Grid:

  def build(grid: Grid): Either[Throwable, Seq[Span]] =
    Right(GridBuilder.build(grid))

  extension (screen: Screen)
    def put(pt: Point, grid: Grid, textStyle: TextStyle): Either[Throwable, Unit] =
      for
        spans <- build(grid)
        _     <- Transform.sequence(spans.zipWithIndex.map { case (span, y) => screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span))) })
      yield ()

    def put(pt: Point, grid: Grid): Either[Throwable, Unit] =
      put(pt, grid, TextStyle.Empty)
