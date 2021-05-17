package com.github.gchudnov.swearwolf.util

object Value {

  def clamp[T: Numeric](min: T, max: T)(value: T): T = {
    val numT = implicitly[Numeric[T]]
    if (numT.lt(value, min))
      min
    else if (numT.gt(value, max))
      max
    else
      value
  }

  /**
   * Scales integral number to [0, ceilY] range
   */
  def scaleSeq[T: Numeric](ceilY: T, maxY: Option[T])(ys: Seq[T]): Seq[T] = {
    val numT = implicitly[Numeric[T]]

    val effectiveMaxY = maxY.getOrElse(numT.max(numT.abs(ys.max), numT.abs(ys.min)))
    ys.map(scaleValue(ceilY, effectiveMaxY)(_))
  }

  private def scaleValue[T: Numeric](ceilY: T, maxY: T)(value: T): T = {
    val numT = implicitly[Numeric[T]]
    if (numT.equiv(value, numT.zero))
      numT.zero
    else
      div(numT.times(value, ceilY), maxY)
  }

  private def div[T: Numeric](a: T, b: T): T = {
    val numT = implicitly[Numeric[T]]
    numT match {
      case _: Integral[T] =>
        implicit val intT: Integral[T] = numT.asInstanceOf[Integral[T]]
        intT.quot(a, b)
      case _: Fractional[T] =>
        implicit val fraT: Fractional[T] = numT.asInstanceOf[Fractional[T]]
        fraT.div(a, b)
      case _ =>
        sys.error("unexpected data type in division")
    }
  }
}
