package com.github.gchudnov.woods.text

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.Point
import com.github.gchudnov.woods.RichText

private[text] class RichTextOps(private val screen: Screen) extends AnyVal {
  def put(pt: Point, value: RichText): Either[Throwable, Unit] =
    screen.put(pt, value.bytes)
}

private[woods] trait RichTextSyntax {
  implicit def richTextOps(screen: Screen): RichTextOps = new RichTextOps(screen)
}
