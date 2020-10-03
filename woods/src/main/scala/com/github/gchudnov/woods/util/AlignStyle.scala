package com.github.gchudnov.woods.util

object AlignStyle {

  sealed trait AlignStyle
  case object Left   extends AlignStyle
  case object Right  extends AlignStyle
  case object Center extends AlignStyle

}
