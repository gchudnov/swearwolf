package com.github.gchudnov.swearwolf.woods

sealed trait GraphStyle

object GraphStyle:
  case object Dot  extends GraphStyle
  case object Step extends GraphStyle
  case object Quad extends GraphStyle
