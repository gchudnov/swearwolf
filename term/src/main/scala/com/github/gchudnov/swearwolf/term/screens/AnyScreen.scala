package com.github.gchudnov.swearwolf.term.screens

import com.github.gchudnov.swearwolf.term.internal.spans.SpanCompiler
import com.github.gchudnov.swearwolf.term.writers.AnyWriter
import com.github.gchudnov.swearwolf.term.{ EscSeq, Screen, Term }
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.{ TextStyle, TextStyleSeq }

abstract class AnyScreen[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyWriter[F](term) with Screen[F]:
  import AnyWriter.*

  override def put(pt: Point, value: String): F[Unit] =
    put(pt, value.getBytes)

  override def put(pt: Point, value: String, style: TextStyle): F[Unit] =
    put(pt, toBytes(value, style))

  override def put(pt: Point, value: Span): F[Unit] =
    val bytes = SpanCompiler.compile(value)
    put(pt, bytes.asArray)

  override def put(pt: Point, value: Array[Byte]): F[Unit] =
    val bytes = EscSeq.cursorPosition(pt).bytes ++ value
    put(bytes)
