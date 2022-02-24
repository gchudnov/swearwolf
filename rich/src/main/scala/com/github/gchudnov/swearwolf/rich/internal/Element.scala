package com.github.gchudnov.swearwolf.rich.internal

private[internal] sealed trait Element

private[internal] final case class TextElement(text: String) extends Element:
  def +(other: TextElement): TextElement = TextElement(text + other.text)

private[internal] case object NewLineElement extends Element

private[internal] final case class TagElement(name: String, value: Option[String], children: Seq[Element]) extends Element

private[internal] object TagElement:
  def empty(name: String): TagElement =
    TagElement(name, None, Nil)

  def apply(name: String, value: String): TagElement =
    TagElement(name, Some(value), Nil)
