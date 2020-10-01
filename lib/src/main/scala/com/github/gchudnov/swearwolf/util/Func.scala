package com.github.gchudnov.swearwolf.util

object Func {

  def sequence[A, B](es: Seq[Either[A, B]]): Either[A, Seq[B]] =
    es.partitionMap(identity) match {
      case (Nil, rights) => Right[A, Seq[B]](rights)
      case (lefts, _)    => Left[A, Seq[B]](lefts.head)
    }

  trait Combinable[A] {
    def combine(lhs: A, rhs: A): A
  }

  implicit class CombinableOps[A: Combinable](lhs: A) {
    def |(rhs: A): A =
      implicitly[Combinable[A]].combine(lhs, rhs)
  }
}
