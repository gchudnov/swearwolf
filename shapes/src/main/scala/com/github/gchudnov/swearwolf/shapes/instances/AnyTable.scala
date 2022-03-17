package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.shapes.table.Table
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait AnyTable:

  extension [F[_]](screen: Screen[F])
    def put(pt: Point, table: Table, textStyle: TextStyle)(using MF: MonadError[F]): F[Unit] =
      Table.put(screen, pt, table, textStyle)

    def put(pt: Point, table: Table)(using MF: MonadError[F]): F[Unit] =
      put(pt, table, TextStyle.Empty)
