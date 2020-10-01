package com.github.gchudnov.swearwolf.util

object TextStyle {

  sealed abstract class TextStyle     extends Style[Text] with Product with Serializable
  case object Empty                   extends TextStyle
  case class Foreground(color: Color) extends TextStyle
  case class Background(color: Color) extends TextStyle
  case object Bold                    extends TextStyle
  case object Italic                  extends TextStyle
  case object Underline               extends TextStyle
  case object Blink                   extends TextStyle
  case object Invert                  extends TextStyle
  case object Strikethrough           extends TextStyle

}
