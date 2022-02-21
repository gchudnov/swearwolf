package com.github.gchudnov.swearwolf.shapes.styles

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.shapes.styles.AlignStyle
import com.github.gchudnov.swearwolf.util.strings.Strings.*

object Layout:

  /**
   * Align text in the given area
   */
  def align(str: String, width: Int, style: AlignStyle): String =
    val offset = style match
      case AlignStyle.Left =>
        0
      case AlignStyle.Right =>
        val offset = width - str.length
        val x      = if offset < 0 then 0 else offset
        x
      case AlignStyle.Center =>
        val offset = (width - str.length) / 2
        val x      = if offset < 0 then 0 else offset
        x

    val lp = str.padLeft(offset + str.length)
    val rp = lp.padRight(width)
    rp
