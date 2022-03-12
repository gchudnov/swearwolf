package com.github.gchudnov.swearwolf.util.colors

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.FutureMonad

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object FutureColor:
  extension (color: Color.type)
    def parseFuture(value: String)(using ec: ExecutionContext): Future[Color] =
      Color.parse[Future](value)
