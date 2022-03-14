package com.github.gchudnov.swearwolf.shapes

import com.github.gchudnov.swearwolf.util.func.EitherMonad

object EitherShapes extends AllShapes[Either[Throwable, *]]
