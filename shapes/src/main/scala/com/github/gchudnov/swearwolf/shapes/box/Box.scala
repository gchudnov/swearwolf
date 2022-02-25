package com.github.gchudnov.swearwolf.shapes.box

import com.github.gchudnov.swearwolf.shapes.box.internal.BoxBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.Transform
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle

final case class Box(size: Size, style: BoxStyle)

object Box:

  def build(box: Box): Either[Throwable, Seq[Span]] =
    Right(BoxBuilder.build(box))

  extension (screen: Screen)
    def put(pt: Point, box: Box, textStyle: TextStyle): Either[Throwable, Unit] =
      for
        spans <- build(box)
        _     <- Transform.sequence(spans.zipWithIndex.map { case (span, y) => screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span))) })
      yield ()

    def put(pt: Point, box: Box): Either[Throwable, Unit] =
      put(pt, box, TextStyle.Empty)
