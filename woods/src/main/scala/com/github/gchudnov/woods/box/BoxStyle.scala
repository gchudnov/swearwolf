package com.github.gchudnov.woods.box

object BoxStyle {

  sealed trait BoxStyle
  case object Empty        extends BoxStyle
  case object SingleBorder extends BoxStyle
  case object DoubleBorder extends BoxStyle
  case object BoldBorder   extends BoxStyle

}
