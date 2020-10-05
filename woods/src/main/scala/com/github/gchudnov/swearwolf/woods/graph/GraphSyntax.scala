package com.github.gchudnov.swearwolf.woods.graph

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point, TextStyle }
import com.github.gchudnov.swearwolf.woods.Graph
import com.github.gchudnov.swearwolf.woods.graph.impl.GraphDrawer

private[graph] class GraphOps(private val screen: Screen) extends AnyVal {
  def put(pt: Point, graph: Graph, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
    GraphDrawer.draw(screen)(pt, graph, textStyle)
}

private[woods] trait GraphSyntax {
  implicit def graphOps(screen: Screen): GraphOps = new GraphOps(screen)
}

object GraphSyntax extends GraphSyntax
