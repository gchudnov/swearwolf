package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.EitherMonad

import scala.annotation.tailrec

/**
 * Synchronous EventLoop.
 */
final class EitherEventLoop(term: Term[Either[Throwable, *]]) extends SyncEventLoop[Either[Throwable, *]](term) {}