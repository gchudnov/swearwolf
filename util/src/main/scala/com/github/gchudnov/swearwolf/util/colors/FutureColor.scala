package com.github.gchudnov.swearwolf.util.colors

import com.github.gchudnov.swearwolf.util.colors.AnyColor
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.FutureMonad

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

final class FutureColor(using ec: ExecutionContext) extends AnyColor[Future]
