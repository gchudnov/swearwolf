package com.github.gchudnov.swearwolf.zio.term

import com.github.gchudnov.swearwolf.term.{ EventLoop, Term }
import com.github.gchudnov.swearwolf.zio.term.ZTerm
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioEventLoop
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

type ZEventLoop = EventLoop[Task]

object ZioEventLoop:
  def layer: URLayer[ZTerm, EventLoop[Task]] =
    (for
      term     <- ZIO.service[Term[Task]]
      eventLoop = new AsyncZioEventLoop(term)
    yield (eventLoop)).toLayer
