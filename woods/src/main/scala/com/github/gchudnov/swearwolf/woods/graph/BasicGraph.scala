package com.github.gchudnov.swearwolf.woods.graph

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.woods.{ Graph, GraphStyle }

private[woods] final case class BasicGraph(size: Size, data: Seq[Double], style: GraphStyle) extends Graph:
  override def add(value: Double): Graph =
    this.copy(data = this.data ++ Seq(value))
