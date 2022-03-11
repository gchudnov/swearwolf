package com.github.gchudnov.swearwolf.zio.term

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioTerm
import com.github.gchudnov.swearwolf.zio.term.internal.RIOMonadAsyncError
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioEventLoop
import zio.*

object ZioEventLoop:
  def layer: URLayer[AsyncZioTerm, AsyncZioEventLoop] =
    (for
      term     <- ZIO.service[AsyncZioTerm]
      eventLoop = new AsyncZioEventLoop(term)
    yield (eventLoop)).toLayer
