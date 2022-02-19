package com.github.gchudnov.swearwolf.woods.table

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.swearwolf.woods.TableStyle
import com.github.gchudnov.swearwolf.woods.table.impl.TableDrawer

private[woods] object TableFactory:

  def estimateSize(data: Seq[Seq[Any]], style: TableStyle): Size =
    TableDrawer.estimateSize(data, style)
