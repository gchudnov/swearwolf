package com.github.gchudnov.swearwolf.shapes.box

import com.github.gchudnov.swearwolf.shapes.box.internal.BoxBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle

final case class Box(size: Size, style: BoxStyle)

object Box:

  def build[F[_]: MonadError](box: Box): F[Seq[Span]] =
    summon[MonadError[F]].succeed(BoxBuilder.build(box))

  def put[F[_]: MonadError](screen: Screen[F], pt: Point, box: Box, textStyle: TextStyle): F[Unit] =
    import MonadError.*

    for
      spans <- build(box)
      _     <- summon[MonadError[F]].sequence(spans.zipWithIndex.map { case (span, y) => screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span))) })
    yield ()
