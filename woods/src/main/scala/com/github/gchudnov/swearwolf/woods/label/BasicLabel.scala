package com.github.gchudnov.swearwolf.woods.label

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.woods.{ AlignStyle, Label }

private[woods] final case class BasicLabel(size: Size, value: String, align: AlignStyle, isFill: Boolean) extends Label
