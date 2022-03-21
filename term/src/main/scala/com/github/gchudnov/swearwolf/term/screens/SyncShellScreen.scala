package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.screens.AnyScreen
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.term.Term.TermAction

abstract class SyncShellScreen[F[_]](term: Term[F], cleanup: TermAction[F])(using ME: MonadError[F]) extends SyncScreen[F](term):
  import MonadError.*

  override def close(): F[Unit] =
    cleanup(term)
      .flatMap(_ => term.close())

// TODO: impl this class, import a method from Either...

// final class EitherScreen(term: Term[Either[Throwable, *]], cleanup: TermAction[Either[Throwable, *]]) extends SyncScreen(term):
