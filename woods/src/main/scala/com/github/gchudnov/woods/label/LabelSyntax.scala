package com.github.gchudnov.woods.label

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{Point, TextStyle}
import com.github.gchudnov.woods.Label
import com.github.gchudnov.woods.label.impl.LabelDrawer

private[label] class LabelOps(private val screen: Screen) extends AnyVal {
  def put(pt: Point, label: Label, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
    LabelDrawer.draw(screen)(pt, label, textStyle)
}

private[woods] trait LabelSyntax {
  implicit def labelOps(screen: Screen): LabelOps = new LabelOps(screen)
}

object LabelSyntax extends LabelSyntax
