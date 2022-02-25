package com.github.gchudnov.swearwolf.example.zio.run

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import zio.*

trait Run:
  def onKeySeq(ks: KeySeq): UIO[Unit]
  def processLoop(): Task[Unit]
  def shutdown(): UIO[Unit]

object Run:
  def onKeySeq(ks: KeySeq): URIO[Run, Unit] = ZIO.serviceWith(_.onKeySeq(ks))
  def processLoop(): RIO[Run, Unit]         = ZIO.serviceWith(_.processLoop())
  def shutdown(): URIO[Run, Any]            = ZIO.serviceWith(_.shutdown())
