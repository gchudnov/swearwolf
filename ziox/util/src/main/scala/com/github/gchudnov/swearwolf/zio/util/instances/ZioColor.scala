package com.github.gchudnov.swearwolf.zio.util.instances

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

private[instances] trait ZioColor:
  extension (color: Color.type)
    def parseZIO(value: String): Task[Color] =
      Color.parse[Task](value)
