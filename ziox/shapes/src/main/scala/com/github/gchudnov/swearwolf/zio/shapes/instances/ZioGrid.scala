package com.github.gchudnov.swearwolf.zio.shapes.instances

import com.github.gchudnov.swearwolf.shapes.grid.Grid
import com.github.gchudnov.swearwolf.shapes.instances.{AnyBox, AnyGrid}
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import com.github.gchudnov.swearwolf.util.func.MonadError
import zio.Task

private[instances] trait ZioGrid extends AnyGrid[Task]:

  extension (gridT: Grid.type)
    def buildZIO(grid: Grid): Task[Seq[Span]] =
      Grid.build[Task](grid)
