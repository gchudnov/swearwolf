package com.github.gchudnov.woods

import com.github.gchudnov.woods.text.RichTextFactory

sealed trait RichText {
  def bytes: Array[Byte]
}

final case class CompiledRichText(bytes: Array[Byte]) extends RichText

/**
 * Style text with the additional attributes.
 *
 * Allowed attributes are:
 *   color, fg
 *   bgcolor, bg
 *   bold, b
 *   italic, i
 *   underline, u
 *   blink, k
 *   invert, v
 *   strikethrough, t
 */
object RichText {

  def make(value: String): Either[Throwable, RichText] =
    RichTextFactory.make(value)

}
