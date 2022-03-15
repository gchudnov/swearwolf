package com.github.gchudnov.swearwolf.zio.term

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioTerm
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioEventLoop
import zio.*

type ZioEventLoop = AsyncZioEventLoop

object ZioEventLoop:
  def layer: URLayer[AsyncZioTerm, ZioEventLoop] =
    (for
      term     <- ZIO.service[AsyncZioTerm]
      eventLoop = new AsyncZioEventLoop(term)
    yield (eventLoop)).toLayer
