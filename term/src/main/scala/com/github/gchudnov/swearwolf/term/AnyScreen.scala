package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import com.github.gchudnov.swearwolf.util.func.MonadError

abstract class AnyScreen[F[_]](term: Term[F])(implicit val ME: MonadError[F]) extends Screen[F] {

  override def put(value: String): F[Unit] =
    put(value.getBytes)

  override def put(value: String, style: TextStyle): F[Unit] =
    put(toBytes(value, style))

  override def put(value: Span): F[Unit] =
    val bytes = TermScreen.compile(value)
    put(bytes.toArray)

  override def put(pt: Point, value: String): F[Unit] =
    put(pt, value.getBytes)

  override def put(pt: Point, value: String, style: TextStyle): F[Unit] =
    put(pt, toBytes(value, style))

  override def put(pt: Point, value: Span): F[Unit] =
    val bytes = TermScreen.compile(value)
    put(pt, bytes.toArray)

  override def put(pt: Point, value: Array[Byte]): F[Unit] =
    if pt.y >= size.height || pt.y < 0 then Right(())
    else
      val bytes = EscSeq.cursorPosition(pt).bytes ++ value
      put(bytes)

}


object AnyScreen:
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
