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
import com.github.gchudnov.swearwolf.example.zio.internal.console.TermConsole
import com.github.gchudnov.swearwolf.example.zio.internal.Logger

object Main extends ZIOAppDefault:

  private val tickDuration = 1000.millis

  override def run: ZIO[Environment with ZEnv with ZIOAppArgs, Any, Any] =
    val env     = makeEnv()
    val program = makeProgram().provideSome[Clock](env)

    Logger.logLn(s"run");

    program
      .tapError(t => printLineError(s"Error: ${t.getMessage}"))

  private def makeProgram(): ZIO[Run with Clock with Screen with EventLoop, Throwable, Unit] =
    for
      eventLoop <- ZIO.service[EventLoop]
      run       <- ZIO.service[Run]
      f0 <- ZStream
              .asyncZIO[Any, Throwable, KeySeq](cb => ZIO(fromCallback(cb, eventLoop)).fork)
              .mapZIO(keySeq => run.onKeySeq(keySeq))
              .runDrain
              .fork
//      f1 <- run.onTick().repeat(Schedule.spaced(tickDuration)).forever.fork
      f2 <- run.messagePump().runDrain.fork
      _  <- f0.join
    yield ()

    // NOTE: the size of the screen is not being handled correctly

  // TODO: is it possible to make the event-loop async ?

  private def fromCallback(cb: Emit[Any, Throwable, KeySeq, Unit], eventLoop: EventLoop): Unit =
    def handler(keySeq: KeySeq): Either[Throwable, EventLoop.Action] =
      // TODO: how to make async ?
      cb(ZIO.succeed(Chunk(keySeq)))
      Right(EventLoop.Action.Continue)

    eventLoop
      .run(handler)
      .fold(err => cb(ZIO.fail(err).mapError(Some(_))), _ => cb(ZIO.fail(None)))

  private def makeEnv(): ZLayer[Any, Throwable, Term with Screen with EventLoop with Run with Console] =
    val termLayer      = TermLayers.termLayer
    val consoleLayer   = termLayer >>> TermConsole.layer
    val screenLayer    = termLayer >>> TermLayers.screenLayer
    val eventLoopLayer = termLayer >>> TermLayers.eventLoopLayer

    val runLayer = screenLayer >>> TermRun.layer

    termLayer ++ screenLayer ++ eventLoopLayer ++ runLayer ++ consoleLayer
