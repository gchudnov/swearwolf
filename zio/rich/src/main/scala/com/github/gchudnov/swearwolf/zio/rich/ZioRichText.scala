package com.github.gchudnov.swearwolf.zio.rich

import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import com.github.gchudnov.swearwolf.rich.internal.AnyRichText
import zio.*

object ZioRichText extends AnyRichText[Task]
