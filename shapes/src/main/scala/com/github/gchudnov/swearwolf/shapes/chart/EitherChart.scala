package com.github.gchudnov.swearwolf.shapes.chart

import com.github.gchudnov.swearwolf.util.func.EitherMonad

object EitherChart extends AnyChart[Either[Throwable, *]]
