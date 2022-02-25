package com.github.gchudnov.swearwolf.example.zio.internal

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import zio.*

trait Run:
  def onKeySeq(ks: KeySeq): UIO[Unit]
  def onTick(): UIO[Unit]
  def keyLoop(): Task[Unit]
  def tickLoop(): Task[Unit]
  def shutdown(): UIO[Unit]

object Run:
  def onKeySeq(ks: KeySeq): URIO[Run, Unit] = ZIO.serviceWith(_.onKeySeq(ks))
  def onTick(): URIO[Run, Unit]             = ZIO.serviceWith(_.onTick())
  def keyLoop(): RIO[Run, Unit]             = ZIO.serviceWith(_.keyLoop())
  def tickLoop(): RIO[Run, Unit]            = ZIO.serviceWith(_.tickLoop())
  def shutdown(): URIO[Run, Any]            = ZIO.serviceWith(_.shutdown())
