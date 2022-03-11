package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.term.Screen

abstract class AnyRichText[F[_]: MonadError]:

  def build(rich: RichText): F[Span] =
    RichText.build(rich)

  extension (screen: Screen[F])
    def put(pt: Point, richText: RichText): F[Unit] =
      import MonadError.*

      for
        span <- build(richText)
        _    <- screen.put(pt, span)
      yield ()
