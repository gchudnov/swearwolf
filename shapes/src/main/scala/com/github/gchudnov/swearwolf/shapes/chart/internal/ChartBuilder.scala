package com.github.gchudnov.swearwolf.shapes.chart.internal

import com.github.gchudnov.swearwolf.shapes.chart.Chart
import com.github.gchudnov.swearwolf.shapes.chart.ChartStyle
import com.github.gchudnov.swearwolf.shapes.chart.ChartStyle.Dot
import com.github.gchudnov.swearwolf.shapes.chart.ChartStyle.Quad
import com.github.gchudnov.swearwolf.shapes.chart.ChartStyle.Step
import com.github.gchudnov.swearwolf.shapes.styles.Symbols
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.numerics.Numerics.*
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle

import scala.collection.immutable.Seq

private[chart] object ChartBuilder:
  private val ceilY = 100.0

  def build(chart: Chart): Seq[Span] =
    val lines = prepare(chart.size, ceilY, None, chart.data, chart.style)
    lines.map(TextSpan(_))

  private[chart] def prepare(sz: Size, ceilY: Double, maxY: Option[Double], data: Seq[Double], style: ChartStyle): Seq[String] =
    val ChartDesc(sx, sy, symbols) = getDesc(style)

    val maxPoints = sz.width * sx
    val curData   = data.takeRight(maxPoints)
    val pad       = maxPoints - curData.size
    val points    = if pad != 0 then Seq.fill(pad)(0.0) ++ data else curData
    val scaled    = points.scaleSeq(ceilY, maxY)

    val dy = ceilY / sz.height // height of one row
    val cy = dy / sy           // height of one y-step in a row

    Range(0, sz.height)
      .map({ h =>
        val y0 = h * dy
        val y1 = y0 + dy

        scaled
          .grouped(sx)
          .map { ds =>
            ds.map(y => yStep(sy, cy)(y0, y1)(y)).mkString("_")
          }
          .map(key => symbols(key))
          .mkString
      })
      .reverse

  private[chart] def yStep(sy: Int, cy: Double)(y0: Double, y1: Double)(y: Double): Int =
    if y >= y1 then sy
    else if y <= y0 then 0
    else
      val u = y - y0
      uStep(cy)(u) + 1

  private[chart] def uStep(cy: Double)(u: Double): Int =
    val p = u / cy
    p.toInt

  private def getDesc(style: ChartStyle): ChartDesc =
    style match
      case Dot =>
        ChartDesc(sx = 2, sy = 4, symbols = Symbols.dotChartUp)
      case Step =>
        ChartDesc(sx = 1, sy = 8, symbols = Symbols.stepChartUp)
      case Quad =>
        ChartDesc(sx = 2, sy = 2, symbols = Symbols.quadChartUp)

  /**
   * Chart description to draw it
   * @param sx
   *   x-steps
   * @param sy
   *   y-steps
   * @param symbols
   *   map to get symbol to represent a cell
   */
  private final case class ChartDesc(sx: Int, sy: Int, symbols: Map[String, String])
