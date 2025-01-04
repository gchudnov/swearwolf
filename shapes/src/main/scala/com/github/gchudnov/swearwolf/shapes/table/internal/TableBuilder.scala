package com.github.gchudnov.swearwolf.shapes.table.internal

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.shapes.table.{ Table, TableStyle }
import com.github.gchudnov.swearwolf.shapes.styles.Symbols
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.TextSpan

import scala.collection.immutable.Seq

private[table] object TableBuilder:

  def build(table: Table): Seq[Span] =
    if table.isEmpty then Seq.empty[Span]
    else
      val lines = prepare(table)
      lines.map(TextSpan(_))

  def estimateSize(table: Table): Size =
    if table.isEmpty then Size(0, 0)
    else
      val td     = getDesc(table.style)
      val widths = colWidths(td, table.rows)
      val row    = widths.map(n => "." * n).mkString(td.vert, td.vert, td.vert)
      val height = 3 + table.height
      Size(width = row.length, height = height)

  private def prepare(table: Table): Seq[String] =
    val td = getDesc(table.style)

    val widths = colWidths(td, table.rows)

    val top    = widths.map(td.horz * _).mkString(td.topLeft, td.ixTop, td.topRight)
    val middle = widths.map(td.horz * _).mkString(td.ixLeft, td.ix, td.ixRight)
    val bottom = widths.map(td.horz * _).mkString(td.bottomLeft, td.ixBottom, td.bottomRight)

    val rows = table.rows.map(_.zip(widths).map { case (item, size) => (" %-" + (size - 1) + "s").format(item) }
      .mkString(td.vert, td.vert, td.vert))

    val rs = top +: rows.head +: middle +: rows.tail :+ bottom
    rs

  private def colWidths(td: TableDesc, rows: Seq[Seq[Any]]) =
    rows.transpose.map(_.map(cell => if cell == null then 0 else cell.toString.length).max + td.pad)

  private def getDesc(style: TableStyle): TableDesc =
    style match
      case TableStyle.Simple =>
        TableDesc(
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
          empty = Symbols.Empty,
          pad = 2,
        )

      case TableStyle.Frame =>
        TableDesc(
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
          empty = Symbols.Empty,
          pad = 2,
        )

  private final case class TableDesc(
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
    empty: String,
    pad: Int,
  )
