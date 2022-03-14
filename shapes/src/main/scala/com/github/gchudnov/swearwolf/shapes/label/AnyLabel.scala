package com.github.gchudnov.swearwolf.shapes.label

import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyLabel[F[_]: MonadError]:

  extension (screen: Screen[F])
    def putLabel(pt: Point, label: Label, textStyle: TextStyle): F[Unit] =
      Label.put(screen, pt, label, textStyle)

    def putLabel(pt: Point, label: Label): F[Unit] =
      putLabel(pt, label, TextStyle.Empty)
