package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.shapes.box.Box
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyBox:

  extension [F[_]](screen: Screen[F])
    def putBox(pt: Point, box: Box, textStyle: TextStyle)(using MF: MonadError[F]): F[Unit] =
      Box.put(screen, pt, box, textStyle)

    def putBox(pt: Point, box: Box)(using MF: MonadError[F]): F[Unit] =
      putBox(pt, box, TextStyle.Empty)
