package com.github.gchudnov.swearwolf.zio.shapes

import com.github.gchudnov.swearwolf.shapes.chart.AnyChart
import com.github.gchudnov.swearwolf.shapes.chart.Chart
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

trait ZioChart extends AnyChart[Task]:

  extension (chartT: Chart.type)
    def buildZIO(chart: Chart): Task[Seq[Span]] =
      Chart.build[Task](chart)
