package com.github.gchudnov.woods.grid

object GridStyle {

  sealed trait GridStyle
  case object Simple    extends GridStyle
  case object Dash2     extends GridStyle
  case object Dash2Bold extends GridStyle
  case object Frame     extends GridStyle
  case object Frame2    extends GridStyle
}
