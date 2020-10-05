package com.github.gchudnov.swearwolf

sealed trait MouseAction

object MouseAction {
  case object Press   extends MouseAction
  case object Release extends MouseAction
}
