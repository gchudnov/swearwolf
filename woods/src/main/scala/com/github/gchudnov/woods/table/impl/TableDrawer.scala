package com.github.gchudnov.woods.table.impl

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{Func, Point, Size, TextStyle}
import com.github.gchudnov.woods.{Table, TableStyle}
import com.github.gchudnov.woods.util.Symbols

private[table] object TableDrawer {

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
    pad: Int
  )

  def estimateSize(data: Seq[Seq[Any]], tableStyle: TableStyle): Size =
    if (data.isEmpty) {
      Size(0, 0)
    } else {
      val td     = getDesc(tableStyle)
      val widths = colWidths(td, data)
      val row    = widths.map(n => "." * n).mkString(td.vert, td.vert, td.vert)
      val height = 3 + data.size
      Size(width = row.length, height = height)
    }

  def draw(screen: Screen)(pt: Point, table: Table, textStyle: TextStyle): Either[Throwable, Unit] = {
    val data = table.data
    if (data.isEmpty) {
      Right[Throwable, Unit](())
    } else {
      val td = getDesc(table.style)

      val widths = colWidths(td, data)

      val top    = widths.map(td.horz * _).mkString(td.topLeft, td.ixTop, td.topRight)
      val middle = widths.map(td.horz * _).mkString(td.ixLeft, td.ix, td.ixRight)
      val bottom = widths.map(td.horz * _).mkString(td.bottomLeft, td.ixBottom, td.bottomRight)

      val rows = data.map(_.zip(widths).map { case (item, size) => (" %-" + (size - 1) + "s").format(item) }
        .mkString(td.vert, td.vert, td.vert))

      val rs = top +: rows.head +: middle +: rows.tail :+ bottom

      for {
        _ <- Func.sequence(rs.zipWithIndex.map({ case (row, i) =>
               screen.put(pt.offset(0, i), row, textStyle)
             }))
      } yield ()
    }
  }

  private def colWidths(td: TableDesc, data: Seq[Seq[Any]]) =
    data.transpose.map(_.map(cell => if (cell == null) 0 else cell.toString.length).max + td.pad)

  private def getDesc(style: TableStyle): TableDesc =
    style match {
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
          pad = 2
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
          pad = 2
        )

    }

}
