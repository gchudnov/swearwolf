package com.github.gchudnov.swearwolf.zio.shapes.instances

import com.github.gchudnov.swearwolf.shapes.instances.AnyLabel
import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.Task

private[instances] trait ZioLabel:

  extension (labelT: Label.type)
    def buildZIO(label: Label): Task[Seq[Span]] =
      Label.build[Task](label)
