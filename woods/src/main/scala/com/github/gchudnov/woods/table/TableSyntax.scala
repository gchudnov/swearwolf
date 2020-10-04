package com.github.gchudnov.woods.table

import com.github.gchudnov.swearwolf.Screen

trait TableSyntax {
  implicit def tableOps(screen: Screen): TableOps = new TableOps(screen)
}
