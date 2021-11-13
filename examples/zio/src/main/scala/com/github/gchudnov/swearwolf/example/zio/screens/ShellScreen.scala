package com.github.gchudnov.swearwolf.example.zio.screens

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.term.ArrayScreen
import com.github.gchudnov.swearwolf.util.Size
import zio._

object ShellScreen {

  val liveLayer: ZServiceBuilder[Any, Throwable, Has[Screen]] =
    ZServiceBuilder.fromAcquireRelease(ZIO.fromEither(Screen.acquire()))(sc => ZIO.fromEither(sc.shutdown()).orDie)

  def testLayer(sz: Size): ZServiceBuilder[Any, Throwable, Has[Screen]] =
    ZServiceBuilder.fromAcquireRelease(ZIO.attempt[Screen](ArrayScreen(sz)))(sc => ZIO.fromEither(sc.shutdown()).orDie)

}
