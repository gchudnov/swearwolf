package com.github.gchudnov.woods

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point, TextStyle }
import com.github.gchudnov.swearwolf.util.TextStyle.TextStyle
import com.github.gchudnov.woods.label.impl.LabelDrawer

package object label {

  implicit class LabelOps(screen: Screen) {

    def put(pt: Point, label: Label, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
      LabelDrawer.draw(screen)(pt, label, textStyle)

  }

}
