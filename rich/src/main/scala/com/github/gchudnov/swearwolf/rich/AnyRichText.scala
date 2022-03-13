package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span

abstract class AnyRichText[F[_]: MonadError]:

  extension (screen: Screen[F])
    def putRich(pt: Point, richText: RichText): F[Unit] =
      RichText.put(screen, pt, richText)
