package com.github.gchudnov.woods.box

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.{ Box, BoxStyle }

private[woods] final case class BasicBox(size: Size, style: BoxStyle) extends Box
