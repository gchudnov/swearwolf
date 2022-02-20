package com.github.gchudnov.swearwolf.draw.graph

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.draw.{ Graph, GraphStyle }

private[draw] final case class BasicGraph(size: Size, data: Seq[Double], style: GraphStyle) extends Graph:
  override def add(value: Double): Graph =
    this.copy(data = this.data ++ Seq(value))
