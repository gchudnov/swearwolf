package com.github.gchudnov.swearwolf.zio.term

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.internal.screens.ShellScreen
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioScreen
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioTerm
import com.github.gchudnov.swearwolf.term.Term.TermAction
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import sun.misc.Signal
import sun.misc.SignalHandler
import zio.*
import zio.stream.*

type ZioScreen = AsyncZioScreen

object ZioScreen:

  def shellLayer: RLayer[AsyncZioTerm, ZioScreen] =
    import Term.*

    val acquire = for
      term <- ZIO.service[AsyncZioTerm]

      resizeFiber <- ZStream
                       .async[Any, Throwable, Unit] { emit =>
                         val handler = new SignalHandler:
                           override def handle(signal: Signal): Unit =
                             emit(term.fetchSize().flatMap(_ => term.flush()).map(Chunk(_)).mapError(Option(_)))

                         Signal.handle(ShellScreen.SIGWINCH, handler)
                       }
                       .runDrain
                       .forkDaemon

      resizeClose: TermAction[Task] = (t: Term[Task]) => resizeFiber.interruptFork

      pairs    = ShellScreen.initRollbackActions[Task]()
      cleanup <- ShellScreen.initTerm(term, pairs)

      screen = new AsyncZioScreen(term, resizeClose | cleanup)
    yield (screen)

    val release = (s: AsyncZioScreen) => s.close().orDie

    ZLayer.fromAcquireRelease(acquire)(release)