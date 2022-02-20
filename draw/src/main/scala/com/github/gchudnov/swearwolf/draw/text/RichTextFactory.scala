package com.github.gchudnov.swearwolf.draw.text

import com.github.gchudnov.swearwolf.draw.RichText
import com.github.gchudnov.swearwolf.draw.text.impl.{ RichTextCompiler, RichTextParser, RichTextStyler }

private[draw] object RichTextFactory:

  def make(value: String): Either[Throwable, RichText] =
    for
      parsed  <- RichTextParser.read(value)
      styled  <- RichTextStyler.style(parsed)
      compiled = RichTextCompiler.compile(styled)
    yield CompiledRichText(compiled)
