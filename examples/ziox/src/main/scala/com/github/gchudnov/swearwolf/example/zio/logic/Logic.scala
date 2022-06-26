package com.github.gchudnov.swearwolf.example.zio.logic

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.zio.term.*
import zio.*
import zio.stream.ZStream

trait Logic:
  def onKeySeq(sz: Option[Size], ks: KeySeq): Task[Unit]

object Logic:

  def layer: ZLayer[ZScreen, Nothing, Logic] =
    ZLayer.fromZIO(for
      screen <- ZIO.service[ZScreen]
      logic   = new LiveLogic(screen)
    yield logic)
