package com.github.gchudnov.swearwolf.zio.rich

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

object ZioRichText:

  extension (richTextT: RichText.type)
    def buildZIO(rich: RichText): Task[Span] =
      RichText.build[Task](rich)

  extension (screen: Screen[Task])
    def put(pt: Point, richText: RichText): Task[Unit] =
      RichText.putScreen(screen, pt, richText)
