package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.AnyWriter
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.term.internal.spans.SpanCompiler

abstract class AnyScreen[F[_]](term: Term[F])(implicit ME: MonadError[F]) extends AnyWriter[F](term)(ME) with Screen[F]:
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
