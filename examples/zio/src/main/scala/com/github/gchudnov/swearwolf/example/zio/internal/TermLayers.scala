package com.github.gchudnov.swearwolf.example.zio.internal

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.Term
import zio.*

object TermLayers:

  /**
   * Low-Level Terminal
   */
  val termLayer: ZLayer[Any, Throwable, Term] =
    (() => Term.make()).toLayer

  /**
   * High-Level Screen
   */
  val screenLayer: ZLayer[Term, Throwable, Screen] =
    def acquire = for
      term   <- ZIO.service[Term]
      screen <- ZIO.fromEither(Screen.make(term))
    yield screen

    def release(s: Screen) = ZIO.attempt(s.close()).orDie

    ZLayer.fromAcquireRelease(acquire)(release)

  /**
   * Event-Loop to process KeySeq events
   */
  val eventLoopLayer: ZLayer[Term, Throwable, EventLoop] =
    (for
      term     <- ZIO.service[Term]
      eventLoop = EventLoop.make(term)
    yield eventLoop).toLayer