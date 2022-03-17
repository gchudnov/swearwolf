package com.github.gchudnov.swearwolf.zio.shapes.instances

import com.github.gchudnov.swearwolf.shapes.chart.Chart
import com.github.gchudnov.swearwolf.shapes.instances.AnyChart
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.Task

private[instances] trait ZioChart:

  extension (chartT: Chart.type)
    def buildZIO(chart: Chart): Task[Seq[Span]] =
      Chart.build[Task](chart)
