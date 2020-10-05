package com.github.gchudnov.swearwolf.woods

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.swearwolf.woods.label.BasicLabel

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
