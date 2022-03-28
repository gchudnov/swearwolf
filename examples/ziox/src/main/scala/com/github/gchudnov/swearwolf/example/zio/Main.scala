package com.github.gchudnov.swearwolf.example.zio

import com.github.gchudnov.swearwolf.example.zio.logic.Logic
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.util.*
import com.github.gchudnov.swearwolf.zio.term.*
import zio.*
import zio.stream.*

/**
 * Interactive Example, that uses StdIn to get input and StdOut to display output.
 *
 * Uses `Task` from ZIO to represent the result of a computation.
 */
object Main extends ZIOAppDefault:

  override def run: ZIO[Environment with ZEnv with ZIOAppArgs, Any, Any] =
    val env     = makeEnv()
    val program = makeProgram().provideSome[Clock](env)

    program

  private def makeProgram(): ZIO[Logic with Clock with ZEventLoop, Throwable, Unit] =
    for
      eventLoop <- ZIO.service[ZEventLoop]
      logic     <- ZIO.service[Logic]
      szRef     <- ZRef.make(None: Option[Size])
      handler = (ks: KeySeq) =>
                  if (ks.isEsc) then ZIO.succeed(EventLoop.Action.Exit)
                  else
                    ks match
                      case SizeKeySeq(sz) =>
                        szRef.updateAndGet(_ => Some(sz)).flatMap(sz => logic.onKeySeq(sz, ks).as(EventLoop.Action.Continue))
                      case _ =>
                        szRef.get.flatMap(sz => logic.onKeySeq(sz, ks).as(EventLoop.Action.Continue))
      _ <- eventLoop.run(handler)
    yield ()

  private def makeEnv(): ZLayer[Any, Throwable, ZTerm with ZScreen with ZEventLoop with Logic] =
    val termLayer      = Term.layer()
    val screenLayer    = termLayer >>> Screen.shellLayer
    val eventLoopLayer = termLayer >>> EventLoop.eventLoopLayer

    val logicLayer = screenLayer >>> Logic.layer

    termLayer ++ screenLayer ++ eventLoopLayer ++ logicLayer
