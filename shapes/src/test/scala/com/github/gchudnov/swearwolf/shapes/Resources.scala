package com.github.gchudnov.swearwolf.shapes

import scala.io.Source
import scala.util.Using

object Resources:

  def string(resourcePath: String): Either[Throwable, String] =
    Using(Source.fromResource(resourcePath)) { source =>
      source.getLines().mkString(sys.props("line.separator"))
    }.toEither
