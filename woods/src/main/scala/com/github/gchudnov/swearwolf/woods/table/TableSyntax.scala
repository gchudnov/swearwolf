package com.github.gchudnov.swearwolf.woods.table

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point, TextStyle }
import com.github.gchudnov.swearwolf.woods.Table
import com.github.gchudnov.swearwolf.woods.table.impl.TableDrawer

private[table] class TableOps(private val screen: Screen) extends AnyVal:
  def put(pt: Point, table: Table, textStyle: TextStyle = TextStyle.Empty): Either[Throwable, Unit] =
    TableDrawer.draw(screen)(pt, table, textStyle)

private[woods] trait TableSyntax:
  implicit def tableOps(screen: Screen): TableOps = new TableOps(screen)

object TableSyntax extends TableSyntax
