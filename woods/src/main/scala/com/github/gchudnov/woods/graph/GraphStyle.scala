package com.github.gchudnov.woods.graph

object GraphStyle {

  sealed trait GraphStyle
  case object Dot  extends GraphStyle
  case object Step extends GraphStyle
  case object Quad extends GraphStyle
}
