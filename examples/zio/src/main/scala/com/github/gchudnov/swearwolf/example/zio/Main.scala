package com.github.gchudnov.swearwolf.example.zio

import com.github.gchudnov.swearwolf._
import com.github.gchudnov.swearwolf.example.zio.run.{ LiveRun, Run }
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
      screen <- ZIO.service[Screen]
      _      <- Run.processLoop().fork
      _ <- ZIO
             .iterate(EventLoop.Action.empty)(EventLoop.isContinue)({ _ =>
               for {
                 ks <- ZIO.fromEither(screen.eventPoll())
                 _  <- ZIO.foreachDiscard(ks)(Run.onKeySeq)
                 a  <- ZIO.fromEither(EventLoop.defaultHandler(ks))
               } yield a
             })
             .ensuring(Run.shutdown())
    } yield ()

  private def makeEnv(): ZServiceBuilder[Any, Throwable, Has[Screen] with Has[Run]] = {
    val screenEnv = ShellScreen.liveLayer // ShellScreen.testLayer(Size(80, 56))
    val runEnv    = LiveRun.layer

    screenEnv ++ (screenEnv >>> runEnv)
  }
}
