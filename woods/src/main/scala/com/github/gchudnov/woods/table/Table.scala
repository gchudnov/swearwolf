package com.github.gchudnov.woods.table

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.table.TableStyle.TableStyle
import com.github.gchudnov.woods.table.impl.TableDrawer

trait Table {
  def data: Seq[Seq[Any]]
  def style: TableStyle
}

object Table {
  def apply(data: Seq[Seq[Any]], style: TableStyle): Table =
    BasicTable(data, style)

  def estimateSize(data: Seq[Seq[Any]], style: TableStyle): Size =
    TableDrawer.estimateSize(data, style)
}
