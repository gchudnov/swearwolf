package com.github.gchudnov.woods.box

import com.github.gchudnov.swearwolf.Screen

trait BoxSyntax {
  implicit def boxOps(screen: Screen): BoxOps = new BoxOps(screen)
}
