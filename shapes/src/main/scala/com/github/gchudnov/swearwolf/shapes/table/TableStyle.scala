package com.github.gchudnov.swearwolf.shapes.table

sealed trait TableStyle

object TableStyle:
  case object Simple extends TableStyle
  case object Frame  extends TableStyle
