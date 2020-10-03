package com.github.gchudnov.woods

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.Point

package object text {

  implicit class RichTextOps(screen: Screen) {
    def put(pt: Point, value: RichText): Either[Throwable, Unit] =
      screen.put(pt, value.bytes)
  }

}
