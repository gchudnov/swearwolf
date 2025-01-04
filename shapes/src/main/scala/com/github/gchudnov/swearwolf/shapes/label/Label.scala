package com.github.gchudnov.swearwolf.shapes.label

import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import com.github.gchudnov.swearwolf.shapes.label.internal.LabelBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.func.MonadError

import scala.collection.immutable.Seq

final case class Label(size: Size, value: String, align: AlignStyle)

object Label:

  def build[F[_]: MonadError](label: Label): F[Seq[Span]] =
    summon[MonadError[F]].succeed(LabelBuilder.build(label))

  def put[F[_]: MonadError](screen: Screen[F], pt: Point, label: Label, textStyle: TextStyle): F[Unit] =
    import MonadError.*

    for
      spans <- build(label)
      _ <- summon[MonadError[F]].sequence(spans.zipWithIndex.map { case (span, y) =>
             screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span)))
           })
    yield ()
