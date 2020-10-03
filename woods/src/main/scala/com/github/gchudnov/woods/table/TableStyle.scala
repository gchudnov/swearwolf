package com.github.gchudnov.woods.table

object TableStyle {

  sealed trait TableStyle
  case object Simple extends TableStyle
  case object Frame  extends TableStyle
}
