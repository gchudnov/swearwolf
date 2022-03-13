package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.FutureMonad
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object FutureRichText:

  extension (richTextT: RichText.type)
    def buildFuture(rich: RichText)(using ec: ExecutionContext): Future[Span] =
      RichText.build[Future](rich)

  extension (screen: Screen[Future])
    def putRich(pt: Point, richText: RichText)(using ec: ExecutionContext): Future[Unit] =
      RichText.put(screen, pt, richText)
