package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Term.TermAction
import com.github.gchudnov.swearwolf.term.{ Screen, Term }
import com.github.gchudnov.swearwolf.util.func.{ IdMonad, Identity, MonadError }

final class IdScreen(term: Term[Identity], cleanup: TermAction[Identity]) extends SyncShellScreen(term, cleanup) {}

object IdScreen:
  def make(term: Term[Identity]): Identity[IdScreen] =
    import MonadError.*

    val cleanupM: Identity[TermAction[Identity[*]]] = SyncShellScreen.initSync(term)
    val screenM                                     = cleanupM.map(cleanup => new IdScreen(term, cleanup))

    screenM
