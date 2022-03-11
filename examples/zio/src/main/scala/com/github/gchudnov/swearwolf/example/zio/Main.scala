package com.github.gchudnov.swearwolf.example.zio

import com.github.gchudnov.swearwolf.example.zio.internal.Logic
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.*
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.zio.term.ZioEventLoop
import com.github.gchudnov.swearwolf.zio.term.ZioScreen
import com.github.gchudnov.swearwolf.zio.term.ZioTerm
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioEventLoop
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioScreen
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioTerm
import zio.*
import zio.stream.*

object Main extends ZIOAppDefault:

  override def run: ZIO[Environment with ZEnv with ZIOAppArgs, Any, Any] =
    val env     = makeEnv()
    val program = makeProgram().provideSome[Clock](env)

    program

  private def makeProgram(): ZIO[Logic with Clock with AsyncZioEventLoop, Throwable, Unit] =
    for
      eventLoop <- ZIO.service[AsyncZioEventLoop]
      logic     <- ZIO.service[Logic]
      handler = (ks: KeySeq) =>
                  if (ks.isEsc) then ZIO.succeed(EventLoop.Action.Exit)
                  else logic.onKeySeq(ks).map(_ => EventLoop.Action.Continue)
      _ <- eventLoop.run(handler)
    yield ()

  private def makeEnv(): ZLayer[Any, Throwable, AsyncZioTerm with AsyncZioScreen with AsyncZioEventLoop with Logic] =
    val termLayer      = ZioTerm.layer
    val screenLayer    = termLayer >>> ZioScreen.shellLayer
    val eventLoopLayer = termLayer >>> ZioEventLoop.layer

    val logicLayer = screenLayer >>> Logic.layer

    termLayer ++ screenLayer ++ eventLoopLayer ++ logicLayer
