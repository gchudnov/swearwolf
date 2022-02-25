package com.github.gchudnov.swearwolf.shapes.label

import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import com.github.gchudnov.swearwolf.shapes.label.internal.LabelBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.Transform
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle

final case class Label(size: Size, value: String, align: AlignStyle)

object Label:

  def build(label: Label): Either[Throwable, Seq[Span]] =
    Right(LabelBuilder.build(label))

  extension (screen: Screen)
    def put(pt: Point, label: Label, textStyle: TextStyle): Either[Throwable, Unit] =
      for
        spans <- build(label)
        _     <- Transform.sequence(spans.zipWithIndex.map { case (span, y) => screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span))) })
      yield ()

    def put(pt: Point, label: Label): Either[Throwable, Unit] =
      put(pt, label, TextStyle.Empty)
