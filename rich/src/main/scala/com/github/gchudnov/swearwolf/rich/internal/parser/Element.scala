package com.github.gchudnov.swearwolf.rich.internal.parser

sealed trait Element

final case class TextElement(text: String) extends Element:
  def +(other: TextElement): TextElement = TextElement(text + other.text)

case object NewLineElement extends Element

final case class TagElement(name: String, value: Option[String], children: Seq[Element]) extends Element

object TagElement:
  def empty(name: String): TagElement =
    TagElement(name, None, Nil)

  def apply(name: String, value: String): TagElement =
    TagElement(name, Some(value), Nil)
