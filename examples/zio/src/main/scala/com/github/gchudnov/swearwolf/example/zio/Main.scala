package com.github.gchudnov.swearwolf.example.zio

import com.github.gchudnov.swearwolf.example.zio.internal.LiveRun
import com.github.gchudnov.swearwolf.example.zio.internal.Run
import com.github.gchudnov.swearwolf.example.zio.internal.ScreenFactory
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.util.*
import zio.Console.printLineError
import zio.*

object Main extends ZIOAppDefault:

  override def run: ZIO[Environment with ZEnv with ZIOAppArgs, Any, Any] =
    val env     = makeEnv()
    val program = makeProgram().provideSome[Clock](env)

    program
      .tapError(t => printLineError(s"Error: ${t.getMessage}"))

  private def makeProgram(): ZIO[Run with Clock with Screen, Throwable, Unit] =
    for
      screen <- ZIO.service[Screen]
      _      <- Run.processLoop().fork
      _ <- ZIO
             .iterate(EventLoop.Action.empty)(EventLoop.isContinue)({ _ =>
               for
                 ks <- ZIO.fromEither(screen.eventPoll())
                 _  <- ZIO.foreachDiscard(ks)(Run.onKeySeq)
                 a  <- ZIO.fromEither(EventLoop.defaultHandler(ks))
               yield a
             })
             .ensuring(Run.shutdown())
    yield ()

  private def makeEnv(): ZLayer[Any, Throwable, Screen with Run] =
    val screenEnv = ScreenFactory.term // ScreenFactory.array(Size(80, 56))
    val runEnv    = LiveRun.layer

    screenEnv ++ (screenEnv >>> runEnv)
