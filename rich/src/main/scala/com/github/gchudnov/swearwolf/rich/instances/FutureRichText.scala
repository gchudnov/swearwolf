package com.github.gchudnov.swearwolf.rich.instances

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.{ Screen, Writer }
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.func.FutureMonad

import scala.concurrent.{ ExecutionContext, Future }

trait FutureRichText:

  extension (richTextT: RichText.type)
    def buildFuture(rich: RichText)(using ec: ExecutionContext): Future[Span] =
      RichText.build[Future](rich)

  extension (writer: Writer[Future])
    def putRich(richText: RichText)(using ec: ExecutionContext): Future[Unit] =
      RichText.put(writer, richText)

  extension (screen: Screen[Future])
    def putRich(pt: Point, richText: RichText)(using ec: ExecutionContext): Future[Unit] =
      RichText.put(screen, pt, richText)

object FutureRichText extends FutureRichText
