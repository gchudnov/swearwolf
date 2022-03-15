package com.github.gchudnov.swearwolf.util.colors.effects

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.IdMonad
import com.github.gchudnov.swearwolf.util.func.Identity

object IdColor:
  extension (color: Color.type)
    def parseId(value: String): Identity[Color] =
      Color.parse[Identity](value)
