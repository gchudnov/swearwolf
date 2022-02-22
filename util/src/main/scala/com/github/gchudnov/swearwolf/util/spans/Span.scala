package com.github.gchudnov.swearwolf.util.spans

import com.github.gchudnov.swearwolf.util.styles.TextStyle

sealed trait Span

case class TextSpan(text: String, style: TextStyle)       extends Span
case class ByteSpan(bytes: Array[Byte], style: TextStyle) extends Span

case class SpanSeq(spans: Seq[Span]) extends Span

// TODO: add Show that can display Span as a string
