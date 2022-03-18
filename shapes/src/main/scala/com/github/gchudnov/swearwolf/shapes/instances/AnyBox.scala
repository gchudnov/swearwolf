package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.shapes.box.Box
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyBox[F[_]: MonadError]:

  extension (screen: Screen[F])
    def putBox(pt: Point, box: Box, textStyle: TextStyle): F[Unit] =
      Box.put(screen, pt, box, textStyle)

    def putBox(pt: Point, box: Box): F[Unit] =
      putBox(pt, box, TextStyle.Empty)
