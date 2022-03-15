package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.term.Writer

trait AnyRichText[F[_]: MonadError]:

  extension (writer: Writer[F])
    def putRich(richText: RichText): F[Unit] =
      RichText.put(writer, richText)

  extension (screen: Screen[F])
    def putRich(pt: Point, richText: RichText): F[Unit] =
      RichText.put(screen, pt, richText)
