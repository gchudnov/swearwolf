package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.Term.TermAction
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.util.geometry.Size
import sun.misc.Signal
import sun.misc.SignalHandler

final class EitherScreen(term: Term[Either[Throwable, *]], cleanup: TermAction[Either[Throwable, *]]) extends SyncScreen(term):

  override def close(): Either[Throwable, Unit] =
    cleanup(term)
      .flatMap(_ => term.close())

// TODO: extract, the code looks common for all of the sync effects
// TODO: add SyncShellScreen ???
object EitherScreen:

  def make(term: Term[Either[Throwable, *]]): Either[Throwable, EitherScreen] =
    val handler = new SignalHandler:
      override def handle(signal: Signal): Unit =
        term.fetchSize().flatMap(_ => term.flush())

    Signal.handle(AnyShellScreen.SIGWINCH, handler)

    val pairs = AnyShellScreen.initRollbackActions[Either[Throwable, *]]()

    for
      cleanup <- AnyShellScreen.init(term, pairs)
      screen   = new EitherScreen(term, cleanup)
    yield screen
