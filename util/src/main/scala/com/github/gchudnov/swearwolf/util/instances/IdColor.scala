package com.github.gchudnov.swearwolf.util.instances

import com.github.gchudnov.swearwolf.util.colors.{Color}
import com.github.gchudnov.swearwolf.util.func.{IdMonad, Identity}

trait IdColor:
  extension (color: Color.type)
    def parseId(value: String): Identity[Color] =
      Color.parse[Identity](value)
