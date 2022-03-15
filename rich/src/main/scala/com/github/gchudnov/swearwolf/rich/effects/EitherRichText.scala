package com.github.gchudnov.swearwolf.rich.effects

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.rich.RichText

object EitherRichText extends AnyRichText[Either[Throwable, *]]:

  extension (richTextT: RichText.type)
    def buildEither(rich: RichText): Either[Throwable, Span] =
      RichText.build[Either[Throwable, *]](rich)
