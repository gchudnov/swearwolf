package com.github.gchudnov.swearwolf.example.zio.internal

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import zio.*

trait Run:
  def onKeySeq(ks: KeySeq): UIO[Unit]
  def onTick(): UIO[Unit]

  // def keyLoop(): Task[Unit]
  // def tickLoop(): Task[Unit]

  def close(): UIO[Unit]
