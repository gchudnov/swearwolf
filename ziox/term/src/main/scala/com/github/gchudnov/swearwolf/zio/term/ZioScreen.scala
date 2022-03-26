package com.github.gchudnov.swearwolf.zio.term

import com.github.gchudnov.swearwolf.term.Term.TermAction
import com.github.gchudnov.swearwolf.term.Term.TermAction.given
import com.github.gchudnov.swearwolf.term.screens.AnyShellScreen
import com.github.gchudnov.swearwolf.term.{ Screen, Term }
import com.github.gchudnov.swearwolf.zio.term.ZTerm
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioScreen
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import sun.misc.{ Signal, SignalHandler }
import zio.*
import zio.stream.*

type ZScreen = Screen[Task]

private[term] trait ZioScreen:
  extension (screenT: Screen.type)
    def shellLayer: RLayer[ZTerm, Screen[Task]] =
      import Term.*

      val acquire = for
        term <- ZIO.service[ZTerm]

        resizeFiber <- ZStream
                         .async[Any, Throwable, Unit] { emit =>
                           val handler = new SignalHandler:
                             override def handle(signal: Signal): Unit =
                               emit((term.fetchSize() *> term.flush()).mapBoth(Option(_), Chunk(_)))

                           Signal.handle(AnyShellScreen.SIGWINCH, handler)
                         }
                         .runDrain
                         .forkDaemon

        resizeClose: TermAction[Task] = (t: Term[Task]) => resizeFiber.interruptFork

        pairs    = AnyShellScreen.initRollbackActions[Task]()
        cleanup <- AnyShellScreen.init(term, pairs)

        screen = new AsyncZioScreen(term, resizeClose | cleanup)
      yield (screen)

      val release = (s: AsyncZioScreen) => s.close().orDie

      ZLayer.fromAcquireRelease(acquire)(release)
