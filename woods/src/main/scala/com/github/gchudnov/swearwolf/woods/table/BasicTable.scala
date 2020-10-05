package com.github.gchudnov.swearwolf.woods.table

import com.github.gchudnov.swearwolf.woods.{ Table, TableStyle }

private[woods] final case class BasicTable(data: Seq[Seq[Any]], style: TableStyle) extends Table
