package com.github.gchudnov.swearwolf.zio.shapes.internal

import com.github.gchudnov.swearwolf.shapes.label.AnyLabel
import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

private[shapes] trait ZioLabel extends AnyLabel[Task]:

  extension (labelT: Label.type)
    def buildZIO(label: Label): Task[Seq[Span]] =
      Label.build[Task](label)
