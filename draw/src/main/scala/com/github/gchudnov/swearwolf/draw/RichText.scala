package com.github.gchudnov.swearwolf.draw

import com.github.gchudnov.swearwolf.draw.text.RichTextFactory

trait RichText:
  def bytes: Array[Byte]

/**
 * Style text with the additional attributes.
 *
 * {{{
 * Allowed attributes are:
 *   color, fg
 *   bgcolor, bg
 *   bold, b
 *   italic, i
 *   underline, u
 *   blink, k
 *   invert, v
 *   strikethrough, t
 * }}}
 */
object RichText:

  def make(value: String): Either[Throwable, RichText] =
    RichTextFactory.make(value)
