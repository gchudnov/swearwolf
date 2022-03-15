package com.github.gchudnov.swearwolf.example.zio.logic

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.zio.term.ZioScreen
import zio.*
import zio.stream.ZStream

trait Logic:
  def onKeySeq(sz: Option[Size], ks: KeySeq): Task[Unit]

object Logic:

  def layer: ZLayer[ZioScreen, Nothing, Logic] =
    (for
      screen <- ZIO.service[ZioScreen]
      logic   = new LiveLogic(screen)
    yield logic).toLayer
