package com.github.gchudnov.swearwolf.example.zio.screens

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.ArrayScreen
import com.github.gchudnov.swearwolf.util.geometry.Size
import zio.*

object ShellScreen:

  val liveLayer: ZLayer[Any, Throwable, Screen] =
    ZLayer.fromAcquireRelease(ZIO.fromEither(Screen.acquire()))(sc => ZIO.fromEither(sc.shutdown()).orDie)

  def testLayer(sz: Size): ZLayer[Any, Throwable, Screen] =
    ZLayer.fromAcquireRelease(ZIO.attempt[Screen](ArrayScreen(sz)))(sc => ZIO.fromEither(sc.shutdown()).orDie)
