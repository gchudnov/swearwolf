package com.github.gchudnov.swearwolf.rich.instances

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.util.geometry.Point

trait EitherRichText extends AnyRichText[Either[Throwable, *]]:

  extension (richTextT: RichText.type)
    def buildEither(rich: RichText): Either[Throwable, Span] =
      RichText.build[Either[Throwable, *]](rich)

object EitherRichText extends EitherRichText
