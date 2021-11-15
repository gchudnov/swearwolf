package com.github.gchudnov.swearwolf.example.zio.run

import com.github.gchudnov.swearwolf.KeySeq
import zio._

trait Run {
  def onKeySeq(ks: KeySeq): UIO[Unit]
  def processLoop(): Task[Unit]
  def shutdown(): UIO[Unit]
}

object Run {
  def onKeySeq(ks: KeySeq): URIO[Has[Run], Unit] = ZIO.serviceWith(_.onKeySeq(ks))
  def processLoop(): RIO[Has[Run], Unit]         = ZIO.serviceWith(_.processLoop())
  def shutdown(): URIO[Has[Run], Any]            = ZIO.serviceWith(_.shutdown())
}
