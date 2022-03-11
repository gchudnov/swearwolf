package com.github.gchudnov.swearwolf.zio

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.zio.internal.AsyncZioTerm
import zio.*

import java.lang.System

object ZioTerm:

  def layer: ULayer[AsyncZioTerm] =
    val in  = System.in
    val out = System.out

    val term = new AsyncZioTerm(in, out, false)
    ZLayer.succeed(term)

    // AsyncTerm[Task] ???
