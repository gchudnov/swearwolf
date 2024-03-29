package com.github.gchudnov.swearwolf.term

import scala.io.Source
import scala.util.Using

object Resources:

  def string(resourcePath: String): Either[Throwable, String] =
    Using(Source.fromResource(resourcePath)) { source =>
      source.getLines().mkString("\n").trim()
    }.toEither
