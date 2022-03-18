package com.github.gchudnov.swearwolf.zio.shapes.instances

import com.github.gchudnov.swearwolf.shapes.instances.{AnyBox, AnyTable}
import com.github.gchudnov.swearwolf.shapes.table.Table
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import com.github.gchudnov.swearwolf.util.func.MonadError
import zio.Task

private[instances] trait ZioTable extends AnyTable[Task]:

  extension (tableT: Table.type)
    def buildZIO(table: Table): Task[Seq[Span]] =
      Table.build[Task](table)
