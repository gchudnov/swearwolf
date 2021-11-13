package com.github.gchudnov.swearwolf.example.zio.run

import com.github.gchudnov.swearwolf.util.EventLoop.KeySeqHandler
import zio.{Clock, Has, RIO, ZIO}

trait Run {
  def eventHandler: ZIO[Any, Throwable, KeySeqHandler]
  def processLoop(): ZIO[Any, Throwable, Unit]
}

object Run {
  def eventHandler: RIO[Has[Run], KeySeqHandler]         = ZIO.serviceWith(_.eventHandler)
  def processLoop(): RIO[Has[Run] with Has[Clock], Unit] = ZIO.serviceWith(_.processLoop())
}
