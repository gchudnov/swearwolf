package com.github.gchudnov.swearwolf.example.zio

import com.github.gchudnov.swearwolf.example.zio.internal.Run
import com.github.gchudnov.swearwolf.example.zio.internal.TermLayers
import com.github.gchudnov.swearwolf.example.zio.internal.TermRun
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.*
import com.github.gchudnov.swearwolf.util.geometry.Size
import zio.Console.printLineError
import zio.*
import zio.stream.ZStream
import zio.stream.ZStream.Emit

object Main extends ZIOAppDefault:

  private val tickDuration = 1000.millis

  override def run: ZIO[Environment with ZEnv with ZIOAppArgs, Any, Any] =
    val env     = makeEnv()
    val program = makeProgram().provideSome[Clock](env)

    program
      .tapError(t => printLineError(s"Error: ${t.getMessage}"))

  private def makeProgram(): ZIO[Run with Clock with Screen with EventLoop, Throwable, Unit] =
    for
      eventLoop <- ZIO.service[EventLoop]
      run       <- ZIO.service[Run]
      f0 <- ZStream
              .async[Any, Throwable, KeySeq](cb => fromCallback(cb, eventLoop))
              .mapZIO(keySeq => run.onKeySeq(keySeq))
              .runDrain
              .fork
      f1 <- run.onTick().repeat(Schedule.spaced(tickDuration)).forever.fork
      f2 <- run.messagePump().runDrain.fork.ensuring(f1.interrupt)
      _  <- f0.join
    yield ()

  private def fromCallback(cb: Emit[Any, Throwable, KeySeq, Unit], eventLoop: EventLoop) =
    def handler(keySeq: KeySeq) =
      cb(ZIO.succeed(Chunk(keySeq)))
      Right(EventLoop.Action.Continue)

    eventLoop
      .run(handler)
      .fold(err => cb(ZIO.fail(err).mapError(Some(_))), _ => cb(ZIO.fail(None)))

  private def makeEnv(): ZLayer[Any, Throwable, Term with Screen with EventLoop with Run] =
    val termLayer      = TermLayers.termLayer
    val screenLayer    = termLayer >>> TermLayers.screenLayer
    val eventLoopLayer = termLayer >>> TermLayers.eventLoopLayer

    val runLayer = screenLayer >>> TermRun.layer

    termLayer ++ screenLayer ++ eventLoopLayer ++ runLayer
