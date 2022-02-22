package com.github.gchudnov.swearwolf.util.show

trait Show[A]:
  extension (a: A) def show: String
