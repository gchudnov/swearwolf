package com.github.gchudnov.woods.graph

import com.github.gchudnov.swearwolf.Screen

trait GraphSyntax {
  implicit def graphOps(screen: Screen): GraphOps = new GraphOps(screen)
}
