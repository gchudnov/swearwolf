package com.github.gchudnov.swearwolf.rich.internal.parser

sealed trait Token

final case class OpenTag(name: String, attr: Option[String] = None) extends Token

final case class CloseTag(name: String) extends Token

final case class Text(value: String) extends Token
