package com.github.gchudnov.swearwolf.util.geometry

final case class Point(x: Int, y: Int):
  def offset(dx: Int, dy: Int): Point = Point(x + dx, y + dy)

object Point:
  val empty: Point = Point(x = 0, y = 0)
