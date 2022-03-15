package com.github.gchudnov.swearwolf.zio.shapes.internal

import com.github.gchudnov.swearwolf.shapes.grid.AnyGrid
import com.github.gchudnov.swearwolf.shapes.grid.Grid
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

private[shapes] trait ZioGrid extends AnyGrid[Task]:

  extension (gridT: Grid.type)
    def buildZIO(grid: Grid): Task[Seq[Span]] =
      Grid.build[Task](grid)
