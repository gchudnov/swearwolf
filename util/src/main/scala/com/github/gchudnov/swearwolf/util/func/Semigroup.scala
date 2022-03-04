package com.github.gchudnov.swearwolf.util.func

import scala.annotation.targetName

trait Semigroup[A]:
  extension (x: A)
    infix def combine(y: A): A

    @targetName("plus") def |(y: A): A = x.combine(y)
