package com.github.gchudnov.swearwolf.shapes.chart

import com.github.gchudnov.swearwolf.shapes.chart.internal.ChartBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.func.MonadError

final case class Chart(size: Size, data: Seq[Double], style: ChartStyle):
  def add(value: Double): Chart =
    this.copy(data = this.data ++ Seq(value))

object Chart:

  def build[F[_]: MonadError](chart: Chart): F[Seq[Span]] =
    given ME: MonadError[F] = summon[MonadError[F]]
    ME.succeed(ChartBuilder.build(chart))

  extension [F[_]: MonadError](screen: Screen[F])
    def put(pt: Point, chart: Chart, textStyle: TextStyle): F[Unit] =
      import MonadError.*
      given ME: MonadError[F] = summon[MonadError[F]]

      for
        spans <- build(chart)
        _     <- ME.sequence(spans.zipWithIndex.map { case (span, y) => screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span))) })
      yield ()

    def put(pt: Point, chart: Chart): F[Unit] =
      put(pt, chart, TextStyle.Empty)
