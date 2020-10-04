package com.github.gchudnov.woods.label

import com.github.gchudnov.swearwolf.Screen

trait LabelSyntax {
  implicit def labelOps(screen: Screen): LabelOps = new LabelOps(screen)
}
