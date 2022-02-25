package com.github.gchudnov.swearwolf.shapes.chart

import com.github.gchudnov.swearwolf.shapes.chart.internal.ChartBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.Transform
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle

final case class Chart(size: Size, data: Seq[Double], style: ChartStyle):
  def add(value: Double): Chart =
    this.copy(data = this.data ++ Seq(value))

object Chart:

  def build(chart: Chart): Either[Throwable, Seq[Span]] =
    Right(ChartBuilder.build(chart))

  extension (screen: Screen)
    def put(pt: Point, chart: Chart, textStyle: TextStyle): Either[Throwable, Unit] =
      for
        spans <- build(chart)
        _     <- Transform.sequence(spans.zipWithIndex.map { case (span, y) => screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span))) })
      yield ()

    def put(pt: Point, chart: Chart): Either[Throwable, Unit] =
      put(pt, chart, TextStyle.Empty)
