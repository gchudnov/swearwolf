package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Term.TermAction
import com.github.gchudnov.swearwolf.term.{ Screen, Term }
import com.github.gchudnov.swearwolf.util.func.TryMonad

import scala.util.Try

final class TryScreen(term: Term[Try], cleanup: TermAction[Try]) extends SyncShellScreen(term, cleanup) {}

object TryScreen:
  def make(term: Term[Try]): Try[TryScreen] =
    import TryMonad.*
    for
      cleanup <- SyncShellScreen.initSync(term)
      screen   = new TryScreen(term, cleanup)
    yield screen
