package com.github.gchudnov.swearwolf.util.instances

import com.github.gchudnov.swearwolf.util.colors.{Color}
import com.github.gchudnov.swearwolf.util.func.TryMonad

import scala.util.Try

trait TryColor:
  extension (colorT: Color.type)
    def parseTry(value: String): Try[Color] =
      Color.parse[Try](value)
