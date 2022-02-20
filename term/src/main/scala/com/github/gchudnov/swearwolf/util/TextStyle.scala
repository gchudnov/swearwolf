package com.github.gchudnov.swearwolf.util

sealed abstract class TextStyle                       extends Product with Serializable
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
