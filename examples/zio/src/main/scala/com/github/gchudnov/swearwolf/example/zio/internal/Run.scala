package com.github.gchudnov.swearwolf.example.zio.internal

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import zio.*
import zio.stream.ZStream

trait Run:
  def onKeySeq(ks: KeySeq): UIO[Unit]
  def onTick(): UIO[Unit]

  def messagePump(): ZStream[Any, Throwable, Unit]

  def close(): UIO[Unit]
