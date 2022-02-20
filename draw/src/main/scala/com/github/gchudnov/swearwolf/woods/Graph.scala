package com.github.gchudnov.swearwolf.draw

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.draw.graph.BasicGraph

trait Graph:
  def size: Size
  def style: GraphStyle
  def data: Seq[Double]

  def add(value: Double): Graph

object Graph:
  def apply(size: Size, data: Seq[Double], style: GraphStyle): Graph =
    BasicGraph(size, data, style)
