package com.github.gchudnov.swearwolf.woods.table

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.woods.Table
import com.github.gchudnov.swearwolf.woods.table.impl.TableDrawer

private[table] trait TableOps:
  extension (screen: Screen)
    def put(pt: Point, table: Table, textStyle: TextStyle): Either[Throwable, Unit] =
      TableDrawer.draw(screen)(pt, table, textStyle)

    def put(pt: Point, table: Table): Either[Throwable, Unit] =
      put(pt, table, TextStyle.Empty)

private[woods] trait TableSyntax extends TableOps

object TableSyntax extends TableSyntax
