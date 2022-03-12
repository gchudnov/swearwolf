package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.rich.internal.Builder
import com.github.gchudnov.swearwolf.rich.internal.Parser
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span

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

