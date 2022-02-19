package com.github.gchudnov.swearwolf.woods

sealed trait TableStyle

object TableStyle:
  case object Simple extends TableStyle
  case object Frame  extends TableStyle
