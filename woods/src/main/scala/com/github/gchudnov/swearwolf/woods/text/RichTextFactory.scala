package com.github.gchudnov.swearwolf.woods.text

import com.github.gchudnov.swearwolf.woods.RichText
import com.github.gchudnov.swearwolf.woods.text.impl.{ RichTextCompiler, RichTextParser, RichTextStyler }

private[woods] object RichTextFactory:

  def make(value: String): Either[Throwable, RichText] =
    for
      parsed  <- RichTextParser.read(value)
      styled  <- RichTextStyler.style(parsed)
      compiled = RichTextCompiler.compile(styled)
    yield CompiledRichText(compiled)
