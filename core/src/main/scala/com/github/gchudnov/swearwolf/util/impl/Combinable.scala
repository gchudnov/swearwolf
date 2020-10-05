package com.github.gchudnov.swearwolf.util.impl

private[util] trait Combinable[A] {
  def combine(lhs: A, rhs: A): A
}

private[util] class CombinableOps[A: Combinable](lhs: A) {
  def |(rhs: A): A =
    implicitly[Combinable[A]].combine(lhs, rhs)
}
