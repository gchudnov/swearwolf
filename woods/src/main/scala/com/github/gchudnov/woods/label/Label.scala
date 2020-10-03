package com.github.gchudnov.woods.label

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.util.AlignStyle
import com.github.gchudnov.woods.util.AlignStyle.AlignStyle

trait Label {
  def value: String
  def size: Size
  def align: AlignStyle
  def isFill: Boolean
}

object Label {
  def apply(size: Size, value: String, align: AlignStyle = AlignStyle.Left, isFill: Boolean = false): Label =
    BasicLabel(size, value, align, isFill)
}
