package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span

object EitherRichText:

  extension (richText: RichText.type)
    def buildEither(rich: RichText): Either[Throwable, Span] =
      RichText.build[Either[Throwable, *]](rich)

  extension (screen: Screen[Either[Throwable, *]])
    def put(pt: Point, richText: RichText): Either[Throwable, Unit] =
      RichText.putScreen(screen, pt, richText)
