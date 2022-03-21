package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.Term.TermAction
import com.github.gchudnov.swearwolf.term.screens.AnyScreen
import com.github.gchudnov.swearwolf.util.func.MonadError
import sun.misc.{ Signal, SignalHandler }

abstract class SyncShellScreen[F[_]](term: Term[F], cleanup: TermAction[F])(using ME: MonadError[F]) extends SyncScreen[F](term):
  import MonadError.*

  override def close(): F[Unit] =
    cleanup(term)
      .flatMap(_ => term.close())

object SyncShellScreen:

  def initSync[F[_]: MonadError](term: Term[F]): F[TermAction[F]] =
    import MonadError.*

    val handler = new SignalHandler:
      override def handle(signal: Signal): Unit =
        term.fetchSize().flatMap(_ => term.flush())

    Signal.handle(AnyShellScreen.SIGWINCH, handler)

    val pairs = AnyShellScreen.initRollbackActions[F]()

    AnyShellScreen.init(term, pairs)
