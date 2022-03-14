package com.github.gchudnov.swearwolf.shapes.box

import com.github.gchudnov.swearwolf.util.func.EitherMonad

object EitherBox extends AnyBox[Either[Throwable, *]]
