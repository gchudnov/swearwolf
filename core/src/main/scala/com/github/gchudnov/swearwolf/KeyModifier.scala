package com.github.gchudnov.swearwolf

sealed trait KeyModifier

object KeyModifier {
  case object Shift extends KeyModifier
  case object Alt   extends KeyModifier
  case object Ctrl  extends KeyModifier
}
