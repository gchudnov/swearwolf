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
import com.github.gchudnov.swearwolf.util.func.MonadError

/**
 * Builds a Span from the parsed elements.
 */
private[rich] object Builder:

  private val lineSep = sys.props("line.separator")

  def build[F[_]: MonadError](elements: Seq[Element]): F[Span] =
    for spans <- Transform.sequence(elements.map(buildSpan))
    yield StyleSpan(TextStyle.empty, spans)

  private def buildSpan[F[_]: MonadError](e: Element): F[Span] =
    import MonadError.*

    given ME: MonadError[F] = summon[MonadError[F]]

    e match
      case TextElement(text) =>
        ME.pure(TextSpan(text))
      case NewLineElement =>
        ME.pure(TextSpan(lineSep))
      case TagElement(name, value, children) =>
        for
          cs    <- Transform.sequence(children.map(buildSpan))
          style <- toTextStyle(name, value)
        yield StyleSpan(style, cs)

  private def toTextStyle[F[_]: MonadError](name: String, value: Option[String]): F[TextStyle] =
    import MonadError.*

    given ME: MonadError[F] = summon[MonadError[F]]

    (name, value) match
      case ("b" | "bold", _) =>
        ME.pure(TextStyle.Bold)
      case ("i" | "italic", _) =>
        ME.pure(TextStyle.Italic)
      case ("u" | "underline", _) =>
        ME.pure(TextStyle.Underline)
      case ("s" | "strikethrough", _) =>
        ME.pure(TextStyle.Strikethrough)
      case ("k" | "blink", _) =>
        ME.pure(TextStyle.Blink)
      case ("v" | "invert", _) =>
        ME.pure(TextStyle.Invert)
      case ("fg" | "fgcolor" | "color", Some(value)) =>
        for color <- Color.parse(value)
        yield TextStyle.Foreground(color)
      case ("bg" | "bgcolor" | "background", Some(value)) =>
        for color <- Color.parse(value)
        yield TextStyle.Background(color)
      case _ =>
        ME.error(new RichTextException(s"Cannot convert Tag(${name}, ${value}) to a TextStyle"))
