package com.github.gchudnov.swearwolf.woods.box

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.swearwolf.woods.{ Box, BoxStyle }

private[woods] final case class BasicBox(size: Size, style: BoxStyle) extends Box
