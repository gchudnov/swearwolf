package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.spans.ByteSpan
import com.github.gchudnov.swearwolf.util.data.Stack
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq

/**
 * Transforms Span to an array of Bytes that can be sent to the terminal.
 */
object SpanCompiler:

  private case class State(
    empty: Int = 0,
    fgColor: Stack[Color] = Stack.empty[Color],
    bgColor: Stack[Color] = Stack.empty[Color],
    bold: Int = 0,
    italic: Int = 0,
    underline: Int = 0,
    blink: Int = 0,
    invert: Int = 0,
    strikethrough: Int = 0,
    transparent: Int = 0,
    noColor: Int = 0
  ):
    def withStyle(style: TextStyle): State = style match
      case TextStyle.Empty             => copy(empty = empty + 1)
      case TextStyle.Bold              => copy(bold = bold + 1)
      case TextStyle.Italic            => copy(italic = italic + 1)
      case TextStyle.Underline         => copy(underline = underline + 1)
      case TextStyle.Blink             => copy(blink = blink + 1)
      case TextStyle.Invert            => copy(invert = invert + 1)
      case TextStyle.Strikethrough     => copy(strikethrough = strikethrough + 1)
      case TextStyle.Transparent       => copy(transparent = transparent + 1)
      case TextStyle.NoColor           => copy(noColor = noColor + 1)
      case TextStyle.Foreground(color) => copy(fgColor = fgColor.push(color))
      case TextStyle.Background(color) => copy(bgColor = bgColor.push(color))
      case TextStyleSeq(styles)        => styles.foldLeft(this)(_.withStyle(_))

  private object State:
    val empty: State =
      State()

  def compile(span: Span): Bytes =

    def iterate(acc: Bytes, state: State, span: Span): Bytes = span match
      case StyleSpan(style, children) =>
        ???

      case TextSpan(text) =>
        acc + Bytes(text.getBytes.toSeq)

      case ByteSpan(bytes) =>
        acc + Bytes(bytes.toSeq)

    iterate(Bytes.empty, State(), span)
