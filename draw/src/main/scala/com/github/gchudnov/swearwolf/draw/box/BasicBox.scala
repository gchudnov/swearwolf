package com.github.gchudnov.swearwolf.draw.box

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.draw.{ Box, BoxStyle }

private[draw] final case class BasicBox(size: Size, style: BoxStyle) extends Box
