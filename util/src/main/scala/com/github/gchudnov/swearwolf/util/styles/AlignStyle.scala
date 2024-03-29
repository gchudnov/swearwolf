package com.github.gchudnov.swearwolf.util.styles

sealed trait AlignStyle

object AlignStyle:
  case object Left   extends AlignStyle
  case object Right  extends AlignStyle
  case object Center extends AlignStyle
