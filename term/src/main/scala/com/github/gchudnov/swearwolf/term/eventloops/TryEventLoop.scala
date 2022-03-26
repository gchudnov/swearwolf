package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.{ EventLoop, Term }
import com.github.gchudnov.swearwolf.util.func.TryMonad

import scala.util.Try

final class TryEventLoop(term: Term[Try]) extends SyncEventLoop[Try](term) {}

object TryEventLoop:

  def make(term: Term[Try]): TryEventLoop =
    new TryEventLoop(term)
