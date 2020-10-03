package com.github.gchudnov.woods.graph

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.graph.GraphStyle.GraphStyle

final case class BasicGraph(size: Size, data: Seq[Double], style: GraphStyle) extends Graph {
  override def add(value: Double): Graph =
    this.copy(data = this.data ++ Seq(value))
}
