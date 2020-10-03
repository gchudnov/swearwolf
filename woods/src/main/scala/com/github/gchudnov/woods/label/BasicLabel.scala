package com.github.gchudnov.woods.label

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.util.AlignStyle.AlignStyle

final case class BasicLabel(size: Size, value: String, align: AlignStyle, isFill: Boolean) extends Label
