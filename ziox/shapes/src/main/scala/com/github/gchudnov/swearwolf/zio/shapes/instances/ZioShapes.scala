package com.github.gchudnov.swearwolf.zio.shapes.instances

import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError

private[instances] sealed trait ZioShapes extends ZioBox with ZioChart with ZioGrid with ZioLabel with ZioTable

object ZioShapes extends ZioShapes
