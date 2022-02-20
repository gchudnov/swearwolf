package com.github.gchudnov.swearwolf.woods.label

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.woods.Label
import com.github.gchudnov.swearwolf.woods.label.impl.LabelDrawer

private[label] trait LabelOps:
  extension (screen: Screen)
    def put(pt: Point, label: Label, textStyle: TextStyle): Either[Throwable, Unit] =
      LabelDrawer.draw(screen)(pt, label, textStyle)

    def put(pt: Point, label: Label): Either[Throwable, Unit] =
      put(pt, label, TextStyle.Empty)

private[woods] trait LabelSyntax extends LabelOps

object LabelSyntax extends LabelSyntax
