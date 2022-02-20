package com.github.gchudnov.swearwolf.util

import com.github.gchudnov.swearwolf.util.impl.{ Combinable, CombinableOps }

private final class CombinableTextStyle extends Combinable[TextStyle]:
  override def combine(lhs: TextStyle, rhs: TextStyle): TextStyle =
    (lhs, rhs) match
      case (a: TextStyleSeq, b: TextStyleSeq) =>
        TextStyleSeq(a.styles ++ b.styles)
      case (a: TextStyleSeq, x) =>
        TextStyleSeq(x +: a.styles)
      case (x, b: TextStyleSeq) =>
        TextStyleSeq(x +: b.styles)
      case (x, y) =>
        TextStyleSeq(Seq(x, y))

private[util] trait TextStyleSyntax:
  private implicit val combinableTextStyle: CombinableTextStyle   = new CombinableTextStyle()
  implicit def styleOps(lhs: TextStyle): CombinableOps[TextStyle] = new CombinableOps[TextStyle](lhs)

object TextStyleSyntax extends TextStyleSyntax
