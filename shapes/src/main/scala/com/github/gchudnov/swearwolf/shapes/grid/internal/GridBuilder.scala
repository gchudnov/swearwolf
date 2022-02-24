package com.github.gchudnov.swearwolf.shapes.grid.internal

import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.shapes.grid.{ Grid, GridStyle }
import com.github.gchudnov.swearwolf.shapes.styles.Symbols
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.TextSpan

private[grid] object GridBuilder:

  def build(grid: Grid): Seq[Span] =
    if grid.size.width < 2 || grid.size.height < 2 then Seq.empty[Span] // Grid is too small to be displayed
    else
      val lines = prepare(grid)
      lines.map(TextSpan(_))

  private def prepare(grid: Grid): Seq[String] =
    val gd = getDesc(grid.style)

    val xis = Range(0, grid.size.width, grid.cell.width).toSet
    val yis = Range(0, grid.size.height, grid.cell.height).toSet

    val top    = gd.topLeft + Range(1, grid.size.width - 1).map(i => if xis.contains(i) then gd.ixTop else gd.horz).mkString + gd.topRight
    val bottom = gd.bottomLeft + Range(1, grid.size.width - 1).map(i => if xis.contains(i) then gd.ixBottom else gd.horz).mkString + gd.bottomRight

    val rows = Range(1, grid.size.height - 1).map { y =>
      Range(0, grid.size.width).map { x =>
        if yis.contains(y) then
          if x == 0 then gd.ixLeft
          else if x == grid.size.width - 1 then gd.ixRight
          else if xis.contains(x) then gd.ix
          else gd.horz
        else if xis.contains(x) || x == grid.size.width - 1 then gd.vert
        else gd.empty
      }.mkString
    }

    Seq(top) ++ rows ++ Seq(bottom)

  private def getDesc(style: GridStyle): GridDesc =
    style match
      case GridStyle.Simple =>
        GridDesc(
          topLeft = Symbols.CharPlus,
          topRight = Symbols.CharPlus,
          bottomLeft = Symbols.CharPlus,
          bottomRight = Symbols.CharPlus,
          ixTop = Symbols.CharPlus,
          ixBottom = Symbols.CharPlus,
          ixLeft = Symbols.CharPlus,
          ixRight = Symbols.CharPlus,
          ix = Symbols.CharPlus,
          horz = Symbols.CharMinus,
          vert = Symbols.CharVertSeparator,
          empty = Symbols.Empty
        )

      case GridStyle.Dash2 =>
        GridDesc(
          topLeft = Symbols.BoxCornerTopLeft,
          topRight = Symbols.BoxCornerTopRight,
          bottomLeft = Symbols.BoxCornerBottomLeft,
          bottomRight = Symbols.BoxCornerBottomRight,
          ixTop = Symbols.BoxIntersectTop,
          ixBottom = Symbols.BoxIntersectBottom,
          ixLeft = Symbols.BoxIntersectLeft,
          ixRight = Symbols.BoxIntersectRight,
          ix = Symbols.BoxIntersect,
          horz = Symbols.BoxDash2Horz,
          vert = Symbols.BoxDash2Vert,
          empty = Symbols.Empty
        )

      case GridStyle.Dash2Bold =>
        GridDesc(
          topLeft = Symbols.BoxCornerHeavyTopLeft,
          topRight = Symbols.BoxCornerHeavyTopRight,
          bottomLeft = Symbols.BoxCornerHeavyBottomLeft,
          bottomRight = Symbols.BoxCornerHeavyBottomRight,
          ixTop = Symbols.BoxIntersectHeavyTop,
          ixBottom = Symbols.BoxIntersectHeavyBottom,
          ixLeft = Symbols.BoxIntersectHeavyLeft,
          ixRight = Symbols.BoxIntersectHeavyRight,
          ix = Symbols.BoxIntersectHeavy,
          horz = Symbols.BoxDash2HeavyHorz,
          vert = Symbols.BoxDash2HeavyVert,
          empty = Symbols.Empty
        )

      case GridStyle.Frame =>
        GridDesc(
          topLeft = Symbols.Frame_EFH,
          topRight = Symbols.Frame_DEH,
          bottomLeft = Symbols.Frame_BEF,
          bottomRight = Symbols.Frame_BED,
          ixTop = Symbols.Frame_DEFH,
          ixBottom = Symbols.Frame_BDEF,
          ixLeft = Symbols.Frame_BEFH,
          ixRight = Symbols.Frame_BDEH,
          ix = Symbols.Frame_BDEFH,
          horz = Symbols.Frame_DEF,
          vert = Symbols.Frame_BEH,
          empty = Symbols.Empty
        )

      case GridStyle.Frame2 =>
        GridDesc(
          topLeft = Symbols.Frame_TQR,
          topRight = Symbols.Frame_QRU,
          bottomLeft = Symbols.Frame_NQR,
          bottomRight = Symbols.Frame_NQP,
          ixTop = Symbols.Frame_PQRT,
          ixBottom = Symbols.Frame_NPQR,
          ixLeft = Symbols.Frame_NQRT,
          ixRight = Symbols.Frame_NPQT,
          ix = Symbols.Frame_NQTPQR,
          horz = Symbols.Frame_PQR,
          vert = Symbols.Frame_NQT,
          empty = Symbols.Empty
        )

  private final case class GridDesc(
    topLeft: String,
    topRight: String,
    bottomLeft: String,
    bottomRight: String,
    ixTop: String,
    ixBottom: String,
    ixLeft: String,
    ixRight: String,
    ix: String,
    horz: String,
    vert: String,
    empty: String
  )
