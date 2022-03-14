package com.github.gchudnov.swearwolf.zio.shapes

import com.github.gchudnov.swearwolf.shapes.box.Box
import com.github.gchudnov.swearwolf.shapes.box.AnyBox
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

trait ZioBox extends AnyBox[Task]:

  extension (boxT: Box.type)
    def buildZIO(box: Box): Task[Seq[Span]] =
      Box.build[Task](box)
