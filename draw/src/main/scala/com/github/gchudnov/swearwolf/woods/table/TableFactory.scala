package com.github.gchudnov.swearwolf.draw.table

import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.draw.TableStyle
import com.github.gchudnov.swearwolf.draw.table.impl.TableDrawer

private[draw] object TableFactory:

  def estimateSize(data: Seq[Seq[Any]], style: TableStyle): Size =
    TableDrawer.estimateSize(data, style)
