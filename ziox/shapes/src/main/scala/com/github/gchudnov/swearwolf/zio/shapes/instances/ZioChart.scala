package com.github.gchudnov.swearwolf.zio.shapes.instances

import com.github.gchudnov.swearwolf.shapes.chart.Chart
import com.github.gchudnov.swearwolf.shapes.instances.{AnyBox, AnyChart}
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import com.github.gchudnov.swearwolf.util.func.MonadError
import zio.Task

private[instances] trait ZioChart extends AnyChart[Task]:

  extension (chartT: Chart.type)
    def buildZIO(chart: Chart): Task[Seq[Span]] =
      Chart.build[Task](chart)
