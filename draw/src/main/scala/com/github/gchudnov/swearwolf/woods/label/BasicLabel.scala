package com.github.gchudnov.swearwolf.draw.label

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.draw.{ AlignStyle, Label }

private[draw] final case class BasicLabel(size: Size, value: String, align: AlignStyle, isFill: Boolean) extends Label
