package com.github.gchudnov.swearwolf.example.zio.internal

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioScreen
import zio.*
import zio.stream.ZStream

trait Logic:
  def onKeySeq(ks: KeySeq): Task[Unit]

object Logic:

  def layer: ZLayer[AsyncZioScreen, Nothing, Logic] =
    (for
      screen <- ZIO.service[AsyncZioScreen]
      logic   = new LiveLogic(screen)
    yield logic).toLayer
