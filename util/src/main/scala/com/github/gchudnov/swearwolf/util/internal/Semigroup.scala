package com.github.gchudnov.swearwolf.util.internal

import scala.annotation.targetName

trait Semigroup[T]:
  extension (x: T)
    infix def combine(y: T): T

    @targetName("plus") def |(y: T): T = x.combine(y)
