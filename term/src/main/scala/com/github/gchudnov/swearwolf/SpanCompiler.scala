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

    // fgColor: Vector[Color] = Vector.empty[Color],
    // bgColor: Vector[Color] = Vector.empty[Color],
    // bold: Vector[Unit] = Vector.empty[Unit],
    // italic: Vector[Unit] = Vector.empty[Unit],
    // underline: Vector[Unit] = Vector.empty[Unit],
    // blink: Vector[Unit] = Vector.empty[Unit],
    // invert: Vector[Unit] = Vector.empty[Unit],
    // strikethrough: Vector[Unit] = Vector.empty[Unit]    
  )

  /*
            case Empty                => ""
          case Foreground(color)    => s"fg(${color.show})"
          case Background(color)    => s"bg(${color.show})"
          case Bold                 => "bold"
          case Italic               => "italic"
          case Underline            => "underline"
          case Blink                => "blink"
          case Invert               => "invert"
          case Strikethrough        => "strikethrough"
          case Transparent          => "transparent"
          case NoColor              => "no-color"
*/


  def compile(span: Span): Bytes = {
    span match {

      case StyleSpan(style, children) =>
        ???

      case TextSpan(text) =>
        ???

      case ByteSpan(bytes) =>
        ???
    }

    ???
  }
