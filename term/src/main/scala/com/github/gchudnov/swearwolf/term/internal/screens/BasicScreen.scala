package com.github.gchudnov.swearwolf.term.internal.screens

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq

private[screens] abstract class BasicScreen extends Screen {
  import BasicScreen.*

  override def put(pt: Point, value: String): Either[Throwable, Unit] =
    put(pt, value.getBytes)

  override def put(pt: Point, value: String, style: TextStyle): Either[Throwable, Unit] =
    val styleBytes = toEscSeq(style).map(_.bytes).reduce(_ ++ _)
    val bytes      = styleBytes ++ value.getBytes ++ EscSeq.reset.bytes
    put(pt, bytes)

  override def put(pt: Point, value: Span): Either[Throwable, Unit] =
    val bytes = TermScreen.compile(value)
    put(pt, bytes.toArray)

}

object BasicScreen:
  private def toEscSeq(style: TextStyle): Seq[EscSeq] =
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