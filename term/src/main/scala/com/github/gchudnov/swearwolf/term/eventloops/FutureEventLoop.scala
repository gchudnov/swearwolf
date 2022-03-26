package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.{ EventLoop, Term }
import com.github.gchudnov.swearwolf.util.func.FutureMonad

import scala.concurrent.{ ExecutionContext, Future }

final class FutureEventLoop(term: Term[Future])(using ec: ExecutionContext) extends AsyncEventLoop[Future](term) {}

object FutureEventLoop:

  def make(term: Term[Future])(using ec: ExecutionContext): FutureEventLoop =
    new FutureEventLoop(term)
