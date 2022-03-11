package com.github.gchudnov.swearwolf.zio.term

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.zio.term.internal.AsyncZioTerm
import zio.*

import java.lang.System

object ZioTerm:

  def layer: ULayer[AsyncZioTerm] =
    val in  = System.in
    val out = System.out

    val term = new AsyncZioTerm(in, out, false)
    ZLayer.succeed(term)
