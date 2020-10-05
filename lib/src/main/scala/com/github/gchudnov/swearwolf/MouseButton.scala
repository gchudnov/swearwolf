package com.github.gchudnov.swearwolf

sealed trait MouseButton

object MouseButton {
  case object Left   extends MouseButton
  case object Middle extends MouseButton
  case object Right  extends MouseButton

  case object ScrollBackward extends MouseButton
  case object ScrollForward  extends MouseButton
}
