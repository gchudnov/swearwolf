package com.github.gchudnov.swearwolf.draw.table

import com.github.gchudnov.swearwolf.draw.{ Table, TableStyle }

private[draw] final case class BasicTable(data: Seq[Seq[Any]], style: TableStyle) extends Table
