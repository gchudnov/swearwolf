package com.github.gchudnov.swearwolf.zio.shapes

import com.github.gchudnov.swearwolf.shapes.AllShapes

import com.github.gchudnov.swearwolf.zio.shapes.internal.{ZioBox, ZioChart, ZioGrid, ZioLabel, ZioTable}
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

object ZioShapes extends ZioBox with ZioChart with ZioGrid with ZioLabel with ZioTable
