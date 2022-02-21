package com.github.gchudnov.swearwolf.draw.table

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.geometry.{ Point }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.draw.Table
import com.github.gchudnov.swearwolf.draw.table.impl.TableDrawer

private[table] trait TableOps:
  extension (screen: Screen)
    def put(pt: Point, table: Table, textStyle: TextStyle): Either[Throwable, Unit] =
      TableDrawer.draw(screen)(pt, table, textStyle)

    def put(pt: Point, table: Table): Either[Throwable, Unit] =
      put(pt, table, TextStyle.Empty)

private[draw] trait TableSyntax extends TableOps

object TableSyntax extends TableSyntax