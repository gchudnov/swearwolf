package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.rich.internal.parser.Parser
import com.github.gchudnov.swearwolf.rich.internal.Builder
import com.github.gchudnov.swearwolf.util.spans.Span

final case class RichText(span: Span)

/**
 * Style text with the additional attributes.
 *
 * {{{
 *   Allowed attributes are:
 *     color, fg
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
  def make(input: String): Either[Throwable, RichText] =
    for
      elements <- Parser.parse(input)
      span     <- Builder.build(elements)
    yield RichText(span)
