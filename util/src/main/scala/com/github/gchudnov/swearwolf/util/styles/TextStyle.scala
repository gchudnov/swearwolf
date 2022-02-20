package com.github.gchudnov.swearwolf.util.styles

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.internal.Monoid

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

  given textStyleMonoid: Monoid[TextStyle] with
    def empty: TextStyle = Empty
    extension (x: TextStyle)
      infix def combine(y: TextStyle): TextStyle =
        (x, y) match
          case (TextStyleSeq(xs), TextStyleSeq(ys)) => TextStyleSeq(xs ++ ys)
          case (TextStyleSeq(xs), _)                => TextStyleSeq(xs :+ y)
          case (_, TextStyleSeq(ys))                => TextStyleSeq(x +: ys)
          case _                                    => TextStyleSeq(Seq(x, y))
