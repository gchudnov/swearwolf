package com.github.gchudnov.swearwolf.term.keys

sealed trait MouseAction

object MouseAction:
  case object Press   extends MouseAction
  case object Release extends MouseAction
