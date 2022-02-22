package com.github.gchudnov.swearwolf.util.spans

import com.github.gchudnov.swearwolf.util.styles.TextStyle

sealed trait Span

final case class TextSpan(text: String, children: Seq[Span], style: TextStyle)       extends Span
final case class ByteSpan(bytes: Array[Byte], children: Seq[Span], style: TextStyle) extends Span

object Span:
  def show: String = ??? // TODO: impl it