package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.rich.internal.Parser
import com.github.gchudnov.swearwolf.rich.internal.Builder
import com.github.gchudnov.swearwolf.util.spans.Span

final case class RichText(input: String)

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
object RichText:

  def build(rich: RichText): Either[Throwable, Span] =
    for
      elements <- Parser.parse(rich.input)
      span     <- Builder.build(elements)
    yield span
