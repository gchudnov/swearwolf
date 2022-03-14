package com.github.gchudnov.swearwolf.shapes

import com.github.gchudnov.swearwolf.shapes.box.AnyBox
import com.github.gchudnov.swearwolf.shapes.chart.AnyChart
import com.github.gchudnov.swearwolf.shapes.grid.AnyGrid
import com.github.gchudnov.swearwolf.shapes.label.AnyLabel
import com.github.gchudnov.swearwolf.shapes.table.AnyTable
import com.github.gchudnov.swearwolf.util.func.MonadError

trait AllShapes[F[_]: MonadError] extends AnyBox[F] with AnyChart[F] with AnyGrid[F] with AnyLabel[F] with AnyTable[F]
