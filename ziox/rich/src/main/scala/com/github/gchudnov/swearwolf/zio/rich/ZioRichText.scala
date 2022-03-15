package com.github.gchudnov.swearwolf.zio.rich

import com.github.gchudnov.swearwolf.rich.effects.AnyRichText
import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

object ZioRichText extends AnyRichText[Task]:

  extension (richTextT: RichText.type)
    def buildZIO(rich: RichText): Task[Span] =
      RichText.build[Task](rich)
