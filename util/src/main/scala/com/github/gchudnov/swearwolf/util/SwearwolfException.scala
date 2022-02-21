package com.github.gchudnov.swearwolf.util

class SwearwolfException(message: String) extends RuntimeException(message):

  def this(message: String, cause: Throwable) =
    this(message)
    initCause(cause)

  def this(cause: Throwable) =
    this(Option(cause).map(_.toString).orNull, cause)

  def this() =
    this(null: String)

object SwearwolfException:
  def unapply(e: SwearwolfException): Option[(String, Throwable)] = Some((e.getMessage, e.getCause))
