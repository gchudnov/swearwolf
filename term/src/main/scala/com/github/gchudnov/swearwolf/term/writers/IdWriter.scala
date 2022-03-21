package com.github.gchudnov.swearwolf.term.writers

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.IdMonad
import com.github.gchudnov.swearwolf.util.func.Identity

final class IdWriter(term: Term[Identity]) extends SyncWriter(term)

object IdWriter:

  def make(term: Term[Identity]): IdWriter =
    new IdWriter(term)
