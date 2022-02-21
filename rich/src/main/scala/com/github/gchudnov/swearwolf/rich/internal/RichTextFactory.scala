package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.rich.internal.{ RichTextCompiler, RichTextParser, RichTextStyler }

private[rich] object RichTextFactory:

  def make(value: String): Either[Throwable, RichText] =
    for
      parsed  <- RichTextParser.read(value)
      styled  <- RichTextStyler.style(parsed)
      compiled = RichTextCompiler.compile(styled)
    yield RichText(compiled)
