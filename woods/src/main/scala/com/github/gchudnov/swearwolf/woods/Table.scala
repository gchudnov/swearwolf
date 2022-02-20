package com.github.gchudnov.swearwolf.woods

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.woods.table.{ BasicTable, TableFactory }

trait Table:
  def data: Seq[Seq[Any]]
  def style: TableStyle

object Table:
  def apply(data: Seq[Seq[Any]], style: TableStyle): Table =
    BasicTable(data, style)

  def estimateSize(data: Seq[Seq[Any]], style: TableStyle): Size =
    TableFactory.estimateSize(data, style)
