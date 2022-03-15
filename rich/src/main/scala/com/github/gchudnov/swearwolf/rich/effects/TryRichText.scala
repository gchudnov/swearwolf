package com.github.gchudnov.swearwolf.rich.effects

import com.github.gchudnov.swearwolf.rich.effects.AnyRichText
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.TryMonad
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.rich.RichText

import scala.util.Try

object TryRichText extends AnyRichText[Try]:

  extension (richTextT: RichText.type)
    def buildTry(rich: RichText): Try[Span] =
      RichText.build[Try](rich)
