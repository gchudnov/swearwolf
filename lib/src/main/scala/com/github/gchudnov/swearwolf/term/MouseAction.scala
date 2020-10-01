package com.github.gchudnov.swearwolf.term

trait MouseAction

object MouseAction {
  case object Press   extends MouseAction
  case object Release extends MouseAction
}
