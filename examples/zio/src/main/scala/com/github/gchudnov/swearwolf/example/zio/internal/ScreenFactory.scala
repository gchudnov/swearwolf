package com.github.gchudnov.swearwolf.example.zio.internal

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.geometry.Size
import zio.*

object ScreenFactory:

  val term: ZLayer[Any, Throwable, Screen] =
    ZLayer.fromAcquireRelease(ZIO.fromEither(Screen.acquire()))(sc => ZIO.fromEither(sc.shutdown()).orDie)

  def array(sz: Size): ZLayer[Any, Throwable, Screen] =
    ZLayer.fromAcquireRelease(ZIO.attempt[Screen](Screen.array(sz)))(sc => ZIO.fromEither(sc.shutdown()).orDie)
