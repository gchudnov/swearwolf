package com.github.gchudnov.swearwolf.woods.text

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.woods.RichText

private[text] trait RichTextOps:
  extension (screen: Screen)
    def put(pt: Point, value: RichText): Either[Throwable, Unit] =
      screen.put(pt, value.bytes)

private[woods] trait RichTextSyntax extends RichTextOps

object RichTextSyntax extends RichTextSyntax
