package com.github.gchudnov.swearwolf.example.zio

import com.github.gchudnov.swearwolf.example.zio.logic.Logic
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.SizeKeySeq
import com.github.gchudnov.swearwolf.util.*
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.zio.term.ZioEventLoop
import com.github.gchudnov.swearwolf.zio.term.ZioScreen
import com.github.gchudnov.swearwolf.zio.term.ZioTerm
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

  private def makeProgram(): ZIO[Logic with Clock with ZioEventLoop, Throwable, Unit] =
    for
      eventLoop <- ZIO.service[ZioEventLoop]
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

  private def makeEnv(): ZLayer[Any, Throwable, ZioTerm with ZioScreen with ZioEventLoop with Logic] =
    val termLayer      = ZioTerm.layer
    val screenLayer    = termLayer >>> ZioScreen.shellLayer
    val eventLoopLayer = termLayer >>> ZioEventLoop.layer

    val logicLayer = screenLayer >>> Logic.layer

    termLayer ++ screenLayer ++ eventLoopLayer ++ logicLayer
