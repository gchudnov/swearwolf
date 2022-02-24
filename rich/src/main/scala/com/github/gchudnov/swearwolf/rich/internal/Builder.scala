package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.internal.Element
import com.github.gchudnov.swearwolf.rich.internal.TextElement
import com.github.gchudnov.swearwolf.rich.internal.NewLineElement
import com.github.gchudnov.swearwolf.rich.internal.TagElement
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.spans.ByteSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle.*
import com.github.gchudnov.swearwolf.util.func.Transform
import scala.util.control.Exception.allCatch
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.rich.RichTextException

/**
 * Builds a Span from the parsed elements.
 */
private[rich] object Builder:

  private val lineSep = sys.props("line.separator")

  def build(elements: Seq[Element]): Either[Throwable, Span] =
    for spans <- Transform.sequence(elements.map(buildSpan))
    yield StyleSpan(TextStyle.empty, spans)

  private def buildSpan(e: Element): Either[Throwable, Span] =
    e match
      case TextElement(text) =>
        Right(TextSpan(text))
      case NewLineElement =>
        Right(TextSpan(lineSep))
      case TagElement(name, value, children) =>
        for
          cs    <- Transform.sequence(children.map(buildSpan))
          style <- toTextStyle(name, value)
        yield StyleSpan(style, cs)

  private def toTextStyle(name: String, value: Option[String]): Either[Throwable, TextStyle] =
    (name, value) match
      case ("b" | "bold", _) =>
        Right(TextStyle.Bold)
      case ("i" | "italic", _) =>
        Right(TextStyle.Italic)
      case ("u" | "underline", _) =>
        Right(TextStyle.Underline)
      case ("s" | "strikethrough", _) =>
        Right(TextStyle.Strikethrough)
      case ("k" | "blink", _) =>
        Right(TextStyle.Blink)
      case ("v" | "invert", _) =>
        Right(TextStyle.Invert)
      case ("fg" | "fgcolor" | "color", Some(value)) =>
        for color <- Color.parse(value)
        yield TextStyle.Foreground(color)
      case ("bg" | "bgcolor" | "background", Some(value)) =>
        for color <- Color.parse(value)
        yield TextStyle.Background(color)
      case _ =>
        Left(new RichTextException(s"Cannot convert Tag(${name}, ${value}) to a TextStyle"))
