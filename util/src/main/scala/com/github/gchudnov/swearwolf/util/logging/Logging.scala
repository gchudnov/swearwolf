package com.github.gchudnov.swearwolf.util.logging

/**
 * Interface for a logger.
 */
trait Logging[F[_]]:
  def log(msg: => String): F[Unit]
