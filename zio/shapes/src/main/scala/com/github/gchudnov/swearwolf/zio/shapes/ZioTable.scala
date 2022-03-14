package com.github.gchudnov.swearwolf.zio.shapes

import com.github.gchudnov.swearwolf.shapes.table.AnyTable
import com.github.gchudnov.swearwolf.shapes.table.Table
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

trait ZioTable extends AnyTable[Task]:

  extension (tableT: Table.type)
    def buildZIO(table: Table): Task[Seq[Span]] =
      Table.build[Task](table)
