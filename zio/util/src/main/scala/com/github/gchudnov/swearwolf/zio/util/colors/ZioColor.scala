package com.github.gchudnov.swearwolf.zio.util.colors

import com.github.gchudnov.swearwolf.util.colors.internal.AnyColor
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

object ZioColor extends AnyColor[Task]
