package com.github.gchudnov.swearwolf.util

import com.github.gchudnov.swearwolf.util.Func.{ Combinable, CombinableOps }

trait Style[T]
sealed trait StyleSeq[T] extends Style[T] {
  def styles: Seq[Style[T]]
}

final case class StyleSeqList[T](styles: Seq[Style[T]]) extends StyleSeq[T]

object Style {

  final class CombinableStyle[T] extends Combinable[Style[T]] {
    override def combine(lhs: Style[T], rhs: Style[T]): Style[T] =
      (lhs, rhs) match {
        case (a: StyleSeq[T], b: StyleSeq[T]) =>
          StyleSeqList(a.styles ++ b.styles)
        case (a: StyleSeq[T], x) =>
          StyleSeqList(x +: a.styles)
        case (x, b: StyleSeq[T]) =>
          StyleSeqList(x +: b.styles)
        case (x, y) =>
          StyleSeqList(Seq(x, y))
      }
  }

  implicit def combinableStyle[T]: CombinableStyle[T] = new CombinableStyle[T]()

  implicit class styleOps[T](lhs: Style[T]) extends CombinableOps[Style[T]](lhs)
}
