package com.github.gchudnov.swearwolf.util

final case class Size(width: Int, height: Int)

object Size {
  val empty: Size = Size(width = 0, height = 0)
}
