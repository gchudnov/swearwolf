package com.github.gchudnov.swearwolf.zio.shapes.instances

import com.github.gchudnov.swearwolf.shapes.box.Box
import com.github.gchudnov.swearwolf.shapes.instances.AnyBox
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import com.github.gchudnov.swearwolf.util.func.MonadError
import zio.Task

private[instances] trait ZioBox extends AnyBox[Task]:

  extension (boxT: Box.type)
    def buildZIO(box: Box): Task[Seq[Span]] =
      Box.build[Task](box)
