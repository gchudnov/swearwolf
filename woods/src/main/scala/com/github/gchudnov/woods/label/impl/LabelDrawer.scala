package com.github.gchudnov.woods.label.impl

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{Func, Point, Text, TextStyle}
import com.github.gchudnov.woods.Label
import com.github.gchudnov.woods.util.Layout

object LabelDrawer {

  def draw(screen: Screen)(pt: Point, label: Label, textStyle: TextStyle): Either[Throwable, Unit] = {
    val lines          = Text.wrap(label.size.width)(label.value)
    val visibleLines   = lines.take(label.size.height)
    val effectiveLines = if (lines.size > visibleLines.size) visibleLines.dropRight(1) :+ Text.forceEllipsisRight(visibleLines.last) else lines

    for {
      _ <- Func.sequence(
             effectiveLines.zipWithIndex
               .map({ case (line, y) =>
                 val x = Layout.align(label.size)(line, label.align).x
                 if (label.isFill) {
                   val updLine = withFilledBackground(label.size.width)(x, line)
                   screen.put(pt.offset(0, y), updLine, textStyle)
                 } else
                   screen.put(pt.offset(x, y), line, textStyle)
               })
           )
    } yield ()
  }

  private[label] def withFilledBackground(width: Int)(x: Int, value: String): String = {
    val lp = Text.padLeft(x + value.length)(value)
    val rp = Text.padRight(width)(lp)
    rp
  }

}
