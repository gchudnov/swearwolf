package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.util.func.FutureMonad

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

sealed abstract class FutureShapes extends AllShapes[Future]

object FutureShapes extends FutureShapes
