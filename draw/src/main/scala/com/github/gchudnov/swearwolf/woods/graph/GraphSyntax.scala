package com.github.gchudnov.swearwolf.draw.graph

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.geometry.{ Point }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.draw.Graph
import com.github.gchudnov.swearwolf.draw.graph.impl.GraphDrawer

private[graph] trait GraphOps:
  extension (screen: Screen)
    def put(pt: Point, graph: Graph, textStyle: TextStyle): Either[Throwable, Unit] =
      GraphDrawer.draw(screen)(pt, graph, textStyle)

    def put(pt: Point, graph: Graph): Either[Throwable, Unit] =
      put(pt, graph, TextStyle.Empty)

private[draw] trait GraphSyntax extends GraphOps

object GraphSyntax extends GraphSyntax
