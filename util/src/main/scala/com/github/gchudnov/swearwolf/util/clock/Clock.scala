package com.github.gchudnov.swearwolf.util.clock

import java.time.Instant

trait Clock[F[_]]:
  def time(): F[Instant]
