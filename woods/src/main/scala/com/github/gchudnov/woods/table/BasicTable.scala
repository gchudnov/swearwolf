package com.github.gchudnov.woods.table

import com.github.gchudnov.woods.table.TableStyle.TableStyle

final case class BasicTable(data: Seq[Seq[Any]], style: TableStyle) extends Table
