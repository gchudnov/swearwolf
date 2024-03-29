package com.github.gchudnov.swearwolf.shapes.instances

import com.github.gchudnov.swearwolf.util.func.EitherMonad

sealed trait EitherShapes extends AllShapes[Either[Throwable, *]]

object EitherShapes extends EitherShapes
