package com.github.gchudnov.swearwolf.zio.shapes

import com.github.gchudnov.swearwolf.shapes.box.Box
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

object ZioBox:

  extension (boxT: Box.type)
    def buildZIO(box: Box): Task[Seq[Span]] =
      Box.build[Task](box)

  extension (screen: Screen[Task])
    def put(pt: Point, box: Box, textStyle: TextStyle): Task[Unit] =
      Box.putScreen(screen, pt, box, textStyle)

    def put(pt: Point, box: Box): Task[Unit] =
      Box.putScreen(screen, pt, box, TextStyle.Empty)
