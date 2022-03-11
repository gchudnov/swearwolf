package com.github.gchudnov.swearwolf.zio

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.zio.internal.AsyncZioTerm
import com.github.gchudnov.swearwolf.zio.internal.RIOMonadAsyncError
import zio.*
import com.github.gchudnov.swearwolf.zio.internal.AsyncZioEventLoop

object ZioEventLoop:
  def layer: URLayer[AsyncZioTerm, AsyncZioEventLoop] =
    (for
      term     <- ZIO.service[AsyncZioTerm]
      eventLoop = new AsyncZioEventLoop(term)
    yield (eventLoop)).toLayer
