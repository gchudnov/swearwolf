package com.github.gchudnov.swearwolf.shapes.chart

sealed trait ChartStyle

object ChartStyle:
  case object Dot  extends ChartStyle
  case object Step extends ChartStyle
  case object Quad extends ChartStyle
