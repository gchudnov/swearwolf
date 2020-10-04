package com.github.gchudnov.woods.table

import com.github.gchudnov.woods.{Table, TableStyle}

final case class BasicTable(data: Seq[Seq[Any]], style: TableStyle) extends Table
