package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span

private[rich] abstract class AnyRichText[F[_]: MonadError]:

  def build(rich: RichText): F[Span] =
    RichText.build(rich)
