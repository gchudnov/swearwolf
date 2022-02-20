package com.github.gchudnov.swearwolf.util.colors

trait ColorOps:
  extension (color: Color)
    /**
     * converts color to a hex value
     * @param color
     *   Color to covert
     * @return
     *   hex value of the color with leading #
     */
    def toHex(): String =
      s"#${f"${color.r}%02x"}${f"${color.g}%02x"}${f"${color.b}%02x"}"

object Colors extends ColorOps
