package com.github.gchudnov.swearwolf.util.numerics

sealed trait NumericOps:

  extension [T: Numeric](value: T)
    def clamp(min: T, max: T): T =
      val numT = summon[Numeric[T]]
      if numT.lt(value, min) then min
      else if numT.gt(value, max) then max
      else value

  extension [T: Numeric](ys: Seq[T])
    /**
     * Scales integral number to [0, ceilY] range
     */
    def scaleSeq(ceilY: T, maxY: Option[T]): Seq[T] =
      val numT = summon[Numeric[T]]

      val effectiveMaxY = maxY.getOrElse(numT.max(numT.abs(ys.max), numT.abs(ys.min)))
      ys.map(scaleValue(ceilY, effectiveMaxY)(_))

  private def scaleValue[T: Numeric](ceilY: T, maxY: T)(value: T): T =
    val numT = summon[Numeric[T]]
    if numT.equiv(value, numT.zero) then numT.zero
    else div(numT.times(value, ceilY), maxY)

  private def div[T: Numeric](a: T, b: T): T =
    val numT = summon[Numeric[T]]
    numT match
      case _: Integral[T] =>
        given intT: Integral[T] = numT.asInstanceOf[Integral[T]]
        intT.quot(a, b)
      case _: Fractional[T] =>
        given fraT: Fractional[T] = numT.asInstanceOf[Fractional[T]]
        fraT.div(a, b)
      case _ =>
        sys.error("Unexpected data type in division")

object Numerics extends NumericOps
