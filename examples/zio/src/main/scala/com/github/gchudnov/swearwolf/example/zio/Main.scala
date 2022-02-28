package com.github.gchudnov.swearwolf.example.zio

import com.github.gchudnov.swearwolf.example.zio.internal.TermRun
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.example.zio.internal.Run
import com.github.gchudnov.swearwolf.example.zio.internal.TermLayers
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.util.*
import com.github.gchudnov.swearwolf.util.geometry.Size
import zio.Console.printLineError
import zio.*
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import zio.stream.ZStream

object Main extends ZIOAppDefault:

  override def run: ZIO[Environment with ZEnv with ZIOAppArgs, Any, Any] =
    val env     = makeEnv()
    val program = makeProgram().provideSome[Clock](env)

    program
      .tapError(t => printLineError(s"Error: ${t.getMessage}"))

  private def makeProgram(): ZIO[Run with Clock with Screen with EventLoop, Throwable, Unit] =
    for
      screen <- ZIO.service[Screen]
      eventLoop <- ZIO.service[EventLoop]
      run <- ZIO.service[Run]
      keySeqStream = ZStream.async[Any, Throwable, KeySeq] { cb =>
        val errOrRes = eventLoop.run(keySeq => { cb(ZIO.succeed(Chunk(keySeq))); Right(EventLoop.Action.Continue) } )
        errOrRes.fold(err => cb(ZIO.fail(err).mapError(Some(_))), _ => cb(ZIO.fail(None)))
      }
      _ <- keySeqStream.map(keySeq => run.onKeySeq(keySeq)).runDrain

      // _ <- ZIO.call


      // _      <- Run.keyLoop().fork
      // _      <- Run.tickLoop().fork
      // _      <- ZIO(Run.onTick()).repeat(Schedule.spaced(1000.millis)).forever.fork
      // _ <- ZIO
      //        .iterate(EventLoop.Action.empty)(_.isContinue)({ _ =>
      //          for
      //            ks <- ZIO.fromEither(screen.eventPoll())
      //            _  <- ZIO.foreachDiscard(ks)(Run.onKeySeq)
      //            a  <- ZIO.fromEither(EventLoop.defaultHandler(ks))
      //          yield a
      //        })
      //        .ensuring(Run.shutdown())
    yield ()

  /*
// Asynchronous Callback-based API
def registerCallback(
    name: String,
    onEvent: Int => Unit,
    onError: Throwable => Unit
): Unit = ???

// Lifting an Asynchronous API to ZStream
val stream = ZStream.async[Any, Throwable, Int] { cb =>
  registerCallback(
    "foo",
    event => cb(ZIO.succeed(Chunk(event))),
    error => cb(ZIO.fail(error).mapError(Some(_)))
  )
}  
  */

    
  private def makeEnv(): ZLayer[Any, Throwable, Term with Screen with EventLoop with Run] =
    val termLayer = TermLayers.termLayer
    val screenLayer = termLayer >>> TermLayers.screenLayer
    val eventLoopLayer = termLayer >>> TermLayers.eventLoopLayer

    val runLayer = screenLayer >>> TermRun.layer

    termLayer ++ screenLayer ++ eventLoopLayer ++ runLayer
