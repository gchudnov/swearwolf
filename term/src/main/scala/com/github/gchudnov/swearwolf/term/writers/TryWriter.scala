package com.github.gchudnov.swearwolf.term.writers

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.TryMonad

import scala.util.Try

final class TryWriter(term: Term[Try]) extends SyncWriter(term)

object TryWriter:

  def make(term: Term[Try]): TryWriter =
    new TryWriter(term)
