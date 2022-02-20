package com.github.gchudnov.swearwolf.util.colors

class ColorException(message: String) extends RuntimeException(message):

  def this(message: String, cause: Throwable) =
    this(message)
    initCause(cause)

  def this(cause: Throwable) =
    this(Option(cause).map(_.toString).orNull, cause)

  def this() =
    this(null: String)

object ColorException:
  def unapply(e: ColorException): Option[(String, Throwable)] = Some((e.getMessage, e.getCause))
