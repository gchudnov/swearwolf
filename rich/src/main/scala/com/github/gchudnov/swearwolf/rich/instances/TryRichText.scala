package com.github.gchudnov.swearwolf.rich.instances

import com.github.gchudnov.swearwolf.rich.internal.AnyRichText
import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.func.TryMonad
import scala.util.Try

trait TryRichText extends AnyRichText[Try]:

  extension (richTextT: RichText.type)
    def buildTry(rich: RichText): Try[Span] =
      RichText.build[Try](rich)
