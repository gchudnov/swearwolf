package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.util.SwearwolfException

class RichTextException(message: String) extends RuntimeException(message):

  def this(message: String, cause: Throwable) =
    this(message)
    initCause(cause)

  def this(cause: Throwable) =
    this(Option(cause).map(_.toString).orNull, cause)

  def this() =
    this(null: String)

object RichTextException:
  def unapply(e: RichTextException): Option[(String, Throwable)] = Some((e.getMessage, e.getCause))
