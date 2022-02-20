package com.github.gchudnov.swearwolf.util.geometry

final case class Rect(pt: Point, sz: Size):

  def left: Int = pt.x
  def top: Int  = pt.y

  def right: Int  = pt.x + sz.width
  def bottom: Int = pt.y + sz.height

  def width: Int  = sz.width
  def height: Int = sz.height

object Rect:
  val empty: Rect = Rect(pt = Point.empty, sz = Size.empty)

  def from(tl: Point, br: Point): Rect =
    Rect(tl, Size(br.x - tl.x, br.y - tl.y))
