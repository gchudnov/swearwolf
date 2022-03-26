package com.github.gchudnov.swearwolf.term.writers

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.FutureMonad

import scala.concurrent.{ ExecutionContext, Future }

final class FutureWriter(term: Term[Future])(using ec: ExecutionContext) extends AsyncWriter(term)

object FutureWriter:

  def make(term: Term[Future])(using ec: ExecutionContext): FutureWriter =
    new FutureWriter(term)
