package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.{ EventLoop, Term }
import com.github.gchudnov.swearwolf.util.func.IdMonad
import com.github.gchudnov.swearwolf.util.func.Identity

final class IdEventLoop(term: Term[Identity]) extends SyncEventLoop[Identity](term) {}

object IdEventLoop:

  def make(term: Term[Identity]): IdEventLoop =
    new IdEventLoop(term)
