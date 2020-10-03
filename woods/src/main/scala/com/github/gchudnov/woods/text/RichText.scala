package com.github.gchudnov.woods.text

import com.github.gchudnov.woods.text.impl.{ RichTextCompiler, RichTextParser, RichTextStyler }

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
    for {
      parsed  <- RichTextParser.read(value)
      styled  <- RichTextStyler.style(parsed)
      compiled = RichTextCompiler.compile(styled)
    } yield CompiledRichText(compiled)

}
