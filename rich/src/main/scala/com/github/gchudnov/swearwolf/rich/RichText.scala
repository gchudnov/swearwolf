package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.rich.internal.Builder
import com.github.gchudnov.swearwolf.rich.internal.Parser
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.term.Writer

/**
 * Style text with the additional attributes.
 *
 * {{{
 *   Allowed attributes are:
 *     fgcolor, fg
 *     bgcolor, bg
 *     bold, b
 *     italic, i
 *     underline, u
 *     blink, k
 *     invert, v
 *     strikethrough, t
 * }}}
 */
final case class RichText(input: String)

object RichText:

  def build[F[_]: MonadError](rich: RichText): F[Span] =
    import MonadError.*

    for
      elements <- Parser.parse(rich.input)
      span     <- Builder.build(elements)
    yield span

  def put[F[_]: MonadError](writer: Writer[F], richText: RichText): F[Unit] =
    import MonadError.*

    for
      span <- build(richText)
      _    <- writer.put(span)
    yield ()

  def put[F[_]: MonadError](screen: Screen[F], pt: Point, richText: RichText): F[Unit] =
    import MonadError.*

    for
      span <- build(richText)
      _    <- screen.put(pt, span)
    yield ()
