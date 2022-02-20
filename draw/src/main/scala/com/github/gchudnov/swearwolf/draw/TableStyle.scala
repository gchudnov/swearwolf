package com.github.gchudnov.swearwolf.draw

sealed trait TableStyle

object TableStyle:
  case object Simple extends TableStyle
  case object Frame  extends TableStyle
