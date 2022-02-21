package com.github.gchudnov.swearwolf.rich.internal.parser

sealed trait Element

final case class TextElement(text: String) extends Element:
  def +(other: TextElement): TextElement = TextElement(text + other.text)

final case class TagElement(name: String, attrValue: Option[String], children: List[Element]) extends Element
