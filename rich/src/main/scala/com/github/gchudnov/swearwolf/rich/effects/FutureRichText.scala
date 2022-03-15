package com.github.gchudnov.swearwolf.rich.effects

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.FutureMonad
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.rich.RichText

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import com.github.gchudnov.swearwolf.term.Writer

object FutureRichText:

  extension (richTextT: RichText.type)
    def buildFuture(rich: RichText)(using ec: ExecutionContext): Future[Span] =
      RichText.build[Future](rich)

  extension (writer: Writer[Future])
    def putRich(richText: RichText)(using ec: ExecutionContext): Future[Unit] =
      RichText.put(writer, richText)

  extension (screen: Screen[Future])
    def putRich(pt: Point, richText: RichText)(using ec: ExecutionContext): Future[Unit] =
      RichText.put(screen, pt, richText)
