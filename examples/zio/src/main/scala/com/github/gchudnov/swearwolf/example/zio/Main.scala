package com.github.gchudnov.swearwolf.example.zio

import com.github.gchudnov.swearwolf._
import com.github.gchudnov.swearwolf.example.zio.run.Run.eventHandler
import com.github.gchudnov.swearwolf.example.zio.run.{LiveRun, Run}
import com.github.gchudnov.swearwolf.example.zio.screens.ShellScreen
import com.github.gchudnov.swearwolf.util._
import zio.Console.printLineError
import zio._

object Main extends ZIOAppDefault {

  override def run: ZIO[Environment with ZEnv with Has[ZIOAppArgs], Any, Any] = {
    val env     = makeEnv()
    val program = makeProgram().provideSomeServices[Has[Clock]](env)

    program
      .tapError(t => printLineError(s"Error: ${t.getMessage}"))
  }

  private def makeProgram(): ZIO[Has[Run] with Has[Clock] with Has[Screen], Throwable, Unit] =
    for {
      screen  <- ZIO.service[Screen]
      handler <- eventHandler
      f1      <- ZIO.fromEither(screen.eventLoop(EventLoop.withDefaultHandler(handler))).fork
      _       <- Run.processLoop().repeat(Schedule.spaced(250.millisecond)).fork
      _       <- f1.join
    } yield ()

  private def makeEnv(): ZServiceBuilder[Any, Throwable, Has[Screen] with Has[Run]] = {
    val screenEnv = ShellScreen.liveLayer // ShellScreen.testLayer(Size(80, 56))
    val runEnv    = LiveRun.layer

    screenEnv ++ (screenEnv >>> runEnv)
  }
}
