package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.util.func.TryMonad

import scala.util.Try

sealed trait TryShapes extends AllShapes[Try]

object TryShapes extends TryShapes
