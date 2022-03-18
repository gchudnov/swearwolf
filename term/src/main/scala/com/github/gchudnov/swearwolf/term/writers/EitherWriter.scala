package com.github.gchudnov.swearwolf.term.writers

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.EitherMonad

final class EitherWriter(term: Term[Either[Throwable, *]]) extends SyncWriter(term)

object EitherWriter:

  def make(term: Term[Either[Throwable, *]]): EitherWriter =
    new EitherWriter(term)
