package com.github.gchudnov.swearwolf.zio

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.internal.screens.ShellScreen
import com.github.gchudnov.swearwolf.zio.internal.AsyncZioScreen
import com.github.gchudnov.swearwolf.zio.internal.AsyncZioTerm
import com.github.gchudnov.swearwolf.zio.internal.RIOMonadAsyncError
import sun.misc.Signal
import sun.misc.SignalHandler
import zio.*

object ZioScreen:

  def shellLayer: RLayer[AsyncZioTerm, AsyncZioScreen] =
    (for
      term             <- ZIO.service[AsyncZioTerm]
      cb: SignalHandler = (sig: Signal) => ()
      pairs             = ShellScreen.initRollbackActions[Task](cb)
      cleanup          <- ShellScreen.initTerm(term, pairs)
      screen            = new AsyncZioScreen(term, cleanup, None)
    yield (screen)).toLayer

    // TODO: ^^^ the impl is very rough, fix it
