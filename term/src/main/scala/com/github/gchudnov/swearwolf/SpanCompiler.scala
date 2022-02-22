package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.spans.ByteSpan
import com.github.gchudnov.swearwolf.util.data.Stack
import com.github.gchudnov.swearwolf.util.colors.Color

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
    transient: Int = 0,
    noColor: Int = 0
  )

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
