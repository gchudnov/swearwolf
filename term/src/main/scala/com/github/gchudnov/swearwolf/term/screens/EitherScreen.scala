package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Term.TermAction
import com.github.gchudnov.swearwolf.term.{ Screen, Term }
import com.github.gchudnov.swearwolf.util.func.EitherMonad

final class EitherScreen(term: Term[Either[Throwable, *]], cleanup: TermAction[Either[Throwable, *]])
    extends SyncShellScreen(term, cleanup) {}

object EitherScreen:
  def make(term: Term[Either[Throwable, *]]): Either[Throwable, EitherScreen] =
    import EitherMonad.*
    for
      cleanup <- SyncShellScreen.initSync(term)
      screen   = new EitherScreen(term, cleanup)
    yield screen
