package com.github.gchudnov.woods

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.TextStyle.TextStyle
import com.github.gchudnov.swearwolf.util.{ Point, TextStyle }
import com.github.gchudnov.woods.table.impl.TableDrawer

package object table {

  implicit class TableOps(screen: Screen) {

    def put(pt: Point, table: Table, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
      TableDrawer.draw(screen)(pt, table, textStyle)

  }

}
