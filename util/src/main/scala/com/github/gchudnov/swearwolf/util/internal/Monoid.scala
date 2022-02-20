package com.github.gchudnov.swearwolf.util.internal

trait Monoid[T] extends Semigroup[T]:
  def empty: T
