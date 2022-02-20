package com.github.gchudnov.swearwolf.shapes.chart

import com.github.gchudnov.swearwolf.util.geometry.Size

final case class Chart(size: Size, data: Seq[Double], style: ChartStyle):
  def add(value: Double): Chart =
    this.copy(data = this.data ++ Seq(value))
