package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.shapes.chart.Chart
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyChart:

  extension [F[_]](screen: Screen[F])
    def put(pt: Point, chart: Chart, textStyle: TextStyle)(using MF: MonadError[F]): F[Unit] =
      Chart.put(screen, pt, chart, textStyle)

    def put(pt: Point, chart: Chart)(using MF: MonadError[F]): F[Unit] =
      put(pt, chart, TextStyle.Empty)
