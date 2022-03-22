package com.github.gchudnov.swearwolf.util.instances

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.FutureMonad

import scala.concurrent.{ ExecutionContext, Future }

private[instances] trait FutureColor:
  extension (colorT: Color.type)
    def parseFuture(value: String)(using ec: ExecutionContext): Future[Color] =
      Color.parse[Future](value)
