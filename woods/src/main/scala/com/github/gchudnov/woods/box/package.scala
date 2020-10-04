package com.github.gchudnov.woods

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point, TextStyle }
import com.github.gchudnov.woods.box.impl.BoxDrawer

package object box {

  class BoxOps(private val screen: Screen) extends AnyVal {
    def put(pt: Point, box: Box, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
      BoxDrawer.draw(screen)(pt, box, textStyle)
  }

}
