package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Term.TermAction
import com.github.gchudnov.swearwolf.term.{ Screen, Term }
import com.github.gchudnov.swearwolf.util.func.{ FutureMonad, MonadError }

import scala.concurrent.{ ExecutionContext, Future }

/**
 * NOTE: it is safe to inherit from SyncScreen since Future is eager.
 */
final class FutureScreen(term: Term[Future], cleanup: TermAction[Future])(using ec: ExecutionContext)
    extends SyncShellScreen(term, cleanup) {}

object FutureScreen:
  def make(term: Term[Future])(using ec: ExecutionContext): Future[FutureScreen] =
    import MonadError.*
    for
      cleanup <- SyncShellScreen.initSync(term)
      screen   = new FutureScreen(term, cleanup)
    yield screen
