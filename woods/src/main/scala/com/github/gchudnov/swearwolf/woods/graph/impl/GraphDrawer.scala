package com.github.gchudnov.swearwolf.woods.graph.impl

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point, Size, TextStyle, Value }
import com.github.gchudnov.swearwolf.woods.{ Graph, GraphStyle }
import com.github.gchudnov.swearwolf.woods.GraphStyle.{ Dot, Quad, Step }
import com.github.gchudnov.swearwolf.woods.util.Symbols
import com.github.gchudnov.swearwolf.woods.util.impl.Func

private[graph] object GraphDrawer {

  def draw(screen: Screen)(pt: Point, graph: Graph, textStyle: TextStyle): Either[Throwable, Unit] = {
    val ceilY = 100.0
    val lines = compile(graph.size, ceilY, None)(graph.data, graph.style)
    for {
      _ <- Func.sequence(lines.zipWithIndex.map { case (line, i) =>
             screen.put(pt.offset(0, i), line, textStyle)
           })
    } yield ()
  }

  def compile(sz: Size, ceilY: Double, maxY: Option[Double])(data: Seq[Double], style: GraphStyle): Seq[String] = {
    val GraphDesc(sx, sy, symbols) = getDesc(style)

    val maxPoints = sz.width * sx
    val curData   = data.takeRight(maxPoints)
    val pad       = maxPoints - curData.size
    val points    = if (pad != 0) Seq.fill(pad)(0.0) ++ data else curData
    val scaled    = Value.scaleSeq(ceilY, maxY)(points)

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

  }

  private[graph] def yStep(sy: Int, cy: Double)(y0: Double, y1: Double)(y: Double): Int =
    if (y >= y1)
      sy
    else if (y <= y0)
      0
    else {
      val u = y - y0
      uStep(cy)(u) + 1
    }

  private[graph] def uStep(cy: Double)(u: Double): Int = {
    val p = u / cy
    p.toInt
  }

  private def getDesc(style: GraphStyle): GraphDesc =
    style match {
      case Dot =>
        GraphDesc(sx = 2, sy = 4, symbols = Symbols.dotGraphUp)
      case Step =>
        GraphDesc(sx = 1, sy = 8, symbols = Symbols.stepGraphUp)
      case Quad =>
        GraphDesc(sx = 2, sy = 2, symbols = Symbols.quadGraphUp)
    }

  /**
   * Graph description to draw it
   * @param sx
   *   x-steps
   * @param sy
   *   y-steps
   * @param symbols
   *   map to get symbol to represent a cell
   */
  private final case class GraphDesc(sx: Int, sy: Int, symbols: Map[String, String])
}
