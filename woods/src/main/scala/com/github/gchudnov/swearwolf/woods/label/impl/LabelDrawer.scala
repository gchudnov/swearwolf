package com.github.gchudnov.swearwolf.woods.label.impl

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.strings.Strings.*
import com.github.gchudnov.swearwolf.woods.Label
import com.github.gchudnov.swearwolf.woods.util.Layout
import com.github.gchudnov.swearwolf.woods.util.impl.Func

private[label] object LabelDrawer:

  def draw(screen: Screen)(pt: Point, label: Label, textStyle: TextStyle): Either[Throwable, Unit] =
    val lines          = label.value.wrap(label.size.width)
    val visibleLines   = lines.take(label.size.height)
    val effectiveLines = if lines.size > visibleLines.size then visibleLines.dropRight(1) :+ visibleLines.last.forceEllipsisRight() else lines

    for _ <- Func.sequence(
               effectiveLines.zipWithIndex.map { case (line, y) =>
                 val x = Layout.align(label.size)(line, label.align).x
                 if label.isFill then
                   val updLine = withFilledBackground(label.size.width)(x, line)
                   screen.put(pt.offset(0, y), updLine, textStyle)
                 else screen.put(pt.offset(x, y), line, textStyle)
               }
             )
    yield ()

  private[label] def withFilledBackground(width: Int)(x: Int, value: String): String =
    val lp = value.padLeft(x + value.length)
    val rp = lp.padRight(width)
    rp
