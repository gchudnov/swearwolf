package com.github.gchudnov.swearwolf.rich.instances

import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.util.func.IdMonad
import com.github.gchudnov.swearwolf.util.func.Identity

trait IdRichText:

  extension (richTextT: RichText.type)
    def buildId(rich: RichText): Identity[Span] =
      RichText.build[Identity](rich)
