package com.github.gchudnov.swearwolf.zio.util.colors

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

object ZioColor:
  extension (color: Color.type)
    def parseZIO(value: String): Task[Color] =
      Color.parse[Task](value)
