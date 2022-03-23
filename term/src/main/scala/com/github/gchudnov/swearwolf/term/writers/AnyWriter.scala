package com.github.gchudnov.swearwolf.term.writers

import com.github.gchudnov.swearwolf.term.{ EscSeq, Term, Writer }
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.term.internal.spans.SpanCompiler

abstract class AnyWriter[F[_]](term: Term[F])(using ME: MonadError[F]) extends Writer[F]:
  import AnyWriter.*

  override def put(value: String): F[Unit] =
    put(value.getBytes)

  override def put(value: String, style: TextStyle): F[Unit] =
    put(toBytes(value, style))

  override def put(value: Span): F[Unit] =
    val bytes = SpanCompiler.compile(value)
    put(bytes.asArray)

  override def put(value: Array[Byte]): F[Unit] =
    term.write(value)

  override def flush(): F[Unit] =
    term.flush()

object AnyWriter:
  def toEscSeq(style: TextStyle): Seq[EscSeq] =
    style match
      case a: TextStyleSeq =>
        a.styles.flatMap(toEscSeq)
      case TextStyle.Empty =>
        Seq(EscSeq.empty)
      case TextStyle.Foreground(color) =>
        Seq(EscSeq.foreground(color))
      case TextStyle.Background(color) =>
        Seq(EscSeq.background(color))
      case TextStyle.Bold =>
        Seq(EscSeq.bold)
      case TextStyle.Italic =>
        Seq(EscSeq.italic)
      case TextStyle.Underline =>
        Seq(EscSeq.underline)
      case TextStyle.Blink =>
        Seq(EscSeq.blink)
      case TextStyle.Invert =>
        Seq(EscSeq.invert)
      case TextStyle.Strikethrough =>
        Seq(EscSeq.strikethrough)
      case TextStyle.Transparent =>
        Seq.empty[EscSeq]
      case TextStyle.NoColor =>
        Seq.empty[EscSeq]

  def toBytes(value: String, style: TextStyle): Array[Byte] =
    val styleBytes = toEscSeq(style).map(_.bytes).reduce(_ ++ _)
    val bytes      = styleBytes ++ value.getBytes ++ EscSeq.reset.bytes
    bytes
