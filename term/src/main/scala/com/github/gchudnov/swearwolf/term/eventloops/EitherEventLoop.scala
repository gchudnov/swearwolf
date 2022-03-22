package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.{ EventLoop, Term }
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.func.EitherMonad

import scala.annotation.tailrec

/**
 * Synchronous EventLoop.
 */
final class EitherEventLoop(term: Term[Either[Throwable, *]]) extends SyncEventLoop[Either[Throwable, *]](term) {}

object EitherEventLoop:

  def make(term: Term[Either[Throwable, *]]): EitherEventLoop =
    new EitherEventLoop(term)
