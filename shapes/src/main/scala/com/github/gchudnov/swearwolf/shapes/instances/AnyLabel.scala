package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyLabel:

  extension [F[_]](screen: Screen[F])
    def put(pt: Point, label: Label, textStyle: TextStyle)(using MF: MonadError[F]): F[Unit] =
      Label.put(screen, pt, label, textStyle)

    def put(pt: Point, label: Label)(using MF: MonadError[F]): F[Unit] =
      put(pt, label, TextStyle.Empty)
