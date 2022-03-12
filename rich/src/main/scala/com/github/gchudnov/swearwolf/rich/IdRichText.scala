package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.IdMonad
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.func.Identity

object IdRichText:

  extension (richTextT: RichText.type)
    def buildId(rich: RichText): Identity[Span] =
      RichText.build[Identity](rich)

  extension (screen: Screen[Identity])
    def put(pt: Point, richText: RichText): Identity[Unit] =
      RichText.putScreen(screen, pt, richText)
