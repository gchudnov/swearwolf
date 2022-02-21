package com.github.gchudnov.swearwolf.shapes.table

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.shapes.table.TableStyle
import com.github.gchudnov.swearwolf.shapes.table.internal.TablePresenter

final case class Table(rows: Seq[Seq[Any]], style: TableStyle):
  def isEmpty: Boolean =
    rows.isEmpty

  def height: Int = 
    rows.size

object Table:
  extension (t: Table)
    def estimateSize(): Size =
      TablePresenter.estimateSize(t)
