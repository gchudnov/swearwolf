package com.github.gchudnov.swearwolf.util.spans

import com.github.gchudnov.swearwolf.util.styles.TextStyle

sealed trait Span

final case class StyleSpan(style: TextStyle, children: Seq[Span]) extends Span
final case class TextSpan(text: String)                           extends Span
final case class ByteSpan(bytes: Array[Byte])                     extends Span

object Span:
  val empty: Span = 
    StyleSpan(TextStyle.empty, Seq.empty[Span])

  def show: String = ??? // TODO: impl it

/*
<fg color="red">
  red
  <ul>text</ul>
</fg>
 */

// TODO: how wrap lines? NewLineSpan ? do we want to encode rect in a span?
// TODO: Span is a Line ?

// TODO: asString to show flattened text
