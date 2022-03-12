package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.rich.internal.AnyRichText
import com.github.gchudnov.swearwolf.util.func.EitherMonad

object EitherRichText extends AnyRichText[Either[Throwable, *]]
