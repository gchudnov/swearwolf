package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.shapes.chart.Chart
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyChart[F[_]: MonadError]:

  extension (screen: Screen[F])
    def putChart(pt: Point, chart: Chart, textStyle: TextStyle): F[Unit] =
      Chart.put(screen, pt, chart, textStyle)

    def putChart(pt: Point, chart: Chart): F[Unit] =
      putChart(pt, chart, TextStyle.Empty)
