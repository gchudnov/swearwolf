package com.github.gchudnov.woods.util

import com.github.gchudnov.swearwolf.util.{ Point, Size }
import com.github.gchudnov.woods.AlignStyle

object Layout {

  /**
   * Align text in the given area
   * Returns the point that text should be printed to be aligned given the top-left coordinates are (0, 0)
   */
  def align(sz: Size)(str: String, style: AlignStyle): Point =
    style match {
      case AlignStyle.Left =>
        Point(0, 0)
      case AlignStyle.Right =>
        val offset = sz.width - str.length
        val x      = if (offset < 0) 0 else offset
        Point(x, 0)
      case AlignStyle.Center =>
        val offset = (sz.width - str.length) / 2
        val x      = if (offset < 0) 0 else offset
        Point(x, 0)
    }

}
