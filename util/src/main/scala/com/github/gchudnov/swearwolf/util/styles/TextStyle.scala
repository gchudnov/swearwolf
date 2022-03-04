package com.github.gchudnov.swearwolf.util.styles

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.Monoid
import com.github.gchudnov.swearwolf.util.show.Show

sealed abstract class TextStyle extends Product with Serializable

final case class TextStyleSeq(styles: Seq[TextStyle]) extends TextStyle

object TextStyle:
  case object Empty                   extends TextStyle
  case class Foreground(color: Color) extends TextStyle
  case class Background(color: Color) extends TextStyle
  case object Bold                    extends TextStyle
  case object Italic                  extends TextStyle
  case object Underline               extends TextStyle
  case object Blink                   extends TextStyle
  case object Invert                  extends TextStyle
  case object Strikethrough           extends TextStyle
  case object Transparent             extends TextStyle // Skip whitespace on rendering
  case object NoColor                 extends TextStyle // Skip color on rendering

  val empty: TextStyle =
    Empty

  extension (style: TextStyle)
    /**
     * Checks whether a style contains the other style
     */
    def contains(needle: TextStyle): Boolean =
      style match
        case TextStyleSeq(styles) =>
          styles.exists(s => s.contains(needle))
        case _ =>
          style == needle

  given textStyleMonoid: Monoid[TextStyle] with
    def empty: TextStyle =
      Empty

    extension (x: TextStyle)
      infix def combine(y: TextStyle): TextStyle =
        (x, y) match
          case (TextStyleSeq(xs), TextStyleSeq(ys)) => TextStyleSeq(xs ++ ys)
          case (TextStyleSeq(xs), _)                => TextStyleSeq(xs :+ y)
          case (_, TextStyleSeq(ys))                => TextStyleSeq(x +: ys)
          case _                                    => TextStyleSeq(Seq(x, y))

  given showTextStyle: Show[TextStyle] with
    extension (a: TextStyle)
      def show: String =
        a match
          case Empty                => ""
          case TextStyleSeq(styles) => styles.map(_.show).mkString(",")
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
