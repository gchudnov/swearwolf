package com.github.gchudnov.swearwolf.zio.term

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioTerm
import zio.*

import java.io.{ InputStream, OutputStream }
import java.lang.System

type ZioTerm = AsyncZioTerm

object ZioTerm:

  def layer(in: InputStream = System.in, out: OutputStream = System.out): ULayer[ZioTerm] =
    val term = new AsyncZioTerm(in, out, false)
    ZLayer.succeed(term)

  def stdIoLayer: ULayer[ZioTerm] =
    layer()
