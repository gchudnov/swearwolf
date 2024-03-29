package com.github.gchudnov.swearwolf.shapes.box

sealed trait BoxStyle

object BoxStyle:
  case object Empty        extends BoxStyle
  case object SingleBorder extends BoxStyle
  case object DoubleBorder extends BoxStyle
  case object BoldBorder   extends BoxStyle
