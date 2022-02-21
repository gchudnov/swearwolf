package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.rich.internal.RichTextFactory

final case class RichText(bytes: Array[Byte])

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
  def make(value: String): Either[Throwable, RichText] =
    RichTextFactory.make(value)
