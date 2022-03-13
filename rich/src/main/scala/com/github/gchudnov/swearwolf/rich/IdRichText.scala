package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.rich.AnyRichText
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.IdMonad
import com.github.gchudnov.swearwolf.util.func.Identity
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span

object IdRichText extends AnyRichText[Identity]:

  extension (richTextT: RichText.type)
    def buildId(rich: RichText): Identity[Span] =
      RichText.build[Identity](rich)
