package com.github.gchudnov.swearwolf.shapes.table

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyTable[F[_]: MonadError]:

  extension (screen: Screen[F])
    def putTable(pt: Point, table: Table, textStyle: TextStyle): F[Unit] =
      Table.put(screen, pt, table, textStyle)

    def putTable(pt: Point, table: Table): F[Unit] =
      putTable(pt, table, TextStyle.Empty)
