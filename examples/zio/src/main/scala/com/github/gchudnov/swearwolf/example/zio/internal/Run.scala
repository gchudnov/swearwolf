package com.github.gchudnov.swearwolf.example.zio.internal

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import zio.*
import zio.stream.ZStream

trait Run:
  def onKeySeq(ks: KeySeq): UIO[Unit] // TODO: probably we don't need that
  def onTick(): UIO[Unit]             // TODO: probably we do not need that

  def messagePump(): ZStream[Any, Throwable, Unit]

  def close(): UIO[Unit]
