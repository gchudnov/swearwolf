package com.github.gchudnov.woods.grid

import com.github.gchudnov.swearwolf.Screen

trait GridSyntax {
  implicit def gridOps(screen: Screen): GridOps = new GridOps(screen)
}
