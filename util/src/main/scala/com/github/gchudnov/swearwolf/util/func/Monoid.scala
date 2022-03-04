package com.github.gchudnov.swearwolf.util.func

trait Monoid[A] extends Semigroup[A]:
  def empty: A
