package com.github.gchudnov.swearwolf.util.exec

import scala.util.control.Exception.nonFatalCatch

object Exec:
  /**
   * Execute the given array of arguments.
   */
  def exec(as: Array[String]): Either[Throwable, Unit] =
    nonFatalCatch.either(Runtime.getRuntime.exec(as))
