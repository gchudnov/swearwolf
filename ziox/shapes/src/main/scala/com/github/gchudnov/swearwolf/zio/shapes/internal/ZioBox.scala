package com.github.gchudnov.swearwolf.zio.shapes.internal

import com.github.gchudnov.swearwolf.shapes.box.Box
import com.github.gchudnov.swearwolf.shapes.box.AnyBox
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

private[shapes] trait ZioBox extends AnyBox[Task]:

  extension (boxT: Box.type)
    def buildZIO(box: Box): Task[Seq[Span]] =
      Box.build[Task](box)