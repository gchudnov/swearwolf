package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.RichTextException
import com.github.gchudnov.swearwolf.rich.internal.Element
import com.github.gchudnov.swearwolf.rich.internal.NewLineElement
import com.github.gchudnov.swearwolf.rich.internal.TagElement
import com.github.gchudnov.swearwolf.rich.internal.TextElement
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.func.MonadError.*
import com.github.gchudnov.swearwolf.util.spans.ByteSpan
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle.*

/**
 * Builds a Span from the parsed elements.
 */
private[rich] object Builder:

  private val lineSep = sys.props("line.separator")

  def build[F[_]: MonadError](elements: Seq[Element]): F[Span] =
    for spans <- summon[MonadError[F]].sequence(elements.map(buildSpan))
    yield StyleSpan(TextStyle.empty, spans)

  private def buildSpan[F[_]: MonadError](e: Element): F[Span] =
    e match
      case TextElement(text) =>
        summon[MonadError[F]].succeed(TextSpan(text))
      case NewLineElement =>
        summon[MonadError[F]].succeed(TextSpan(lineSep))
      case TagElement(name, value, children) =>
        for
          cs    <- summon[MonadError[F]].sequence(children.map(buildSpan))
          style <- toTextStyle(name, value)
        yield StyleSpan(style, cs)

  private def toTextStyle[F[_]](name: String, value: Option[String])(using ME: MonadError[F]): F[TextStyle] =
    (name, value) match
      case ("b" | "bold", _) =>
        ME.succeed(TextStyle.Bold)
      case ("i" | "italic", _) =>
        ME.succeed(TextStyle.Italic)
      case ("u" | "underline", _) =>
        ME.succeed(TextStyle.Underline)
      case ("s" | "strikethrough", _) =>
        ME.succeed(TextStyle.Strikethrough)
      case ("k" | "blink", _) =>
        ME.succeed(TextStyle.Blink)
      case ("v" | "invert", _) =>
        ME.succeed(TextStyle.Invert)
      case ("fg" | "fgcolor" | "color", Some(value)) =>
        for color <- Color.parse(value)
        yield TextStyle.Foreground(color)
      case ("bg" | "bgcolor" | "background", Some(value)) =>
        for color <- Color.parse(value)
        yield TextStyle.Background(color)
      case _ =>
        ME.fail(new RichTextException(s"Cannot convert Tag(${name}, ${value}) to a TextStyle"))
