package com.github.gchudnov.woods.table

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.woods.TableStyle
import com.github.gchudnov.woods.table.impl.TableDrawer

private[woods] object TableFactory {

  def estimateSize(data: Seq[Seq[Any]], style: TableStyle): Size =
    TableDrawer.estimateSize(data, style)

}
