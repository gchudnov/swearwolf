package com.github.gchudnov.swearwolf.util.spans

import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.show.Show
import com.github.gchudnov.swearwolf.util.bytes.Bytes

sealed trait Span

final case class StyleSpan(style: TextStyle, children: Seq[Span]) extends Span
final case class TextSpan(text: String)                           extends Span
final case class ByteSpan(bytes: Array[Byte])                     extends Span

object Span:
  val empty: Span =
    StyleSpan(TextStyle.empty, Seq.empty[Span])

  given showSpan: Show[Span] with
    extension (a: Span)
      def show: String =
        a match
          case StyleSpan(style, children) =>
            val childrenStr = children.map(showSpan.show).mkString("")
            s"[${style.show}]($childrenStr)"
          case TextSpan(text) =>
            s"${text}"
          case ByteSpan(bytes) =>
            Bytes(bytes).show
