package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle

/**
 * Interface to write to a terminal.
 *
 * It is more high-level than writing to the terminal directly that accepts only bytes.
 */
trait Writer[F[_]]:
  def put(value: String): F[Unit]
  def put(value: String, style: TextStyle): F[Unit]
  def put(value: Span): F[Unit]
  def put(value: Array[Byte]): F[Unit]

  def flush(): F[Unit]
