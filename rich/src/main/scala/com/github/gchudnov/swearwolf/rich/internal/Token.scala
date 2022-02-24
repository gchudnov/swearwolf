package com.github.gchudnov.swearwolf.rich.internal

private[internal] sealed trait Token

private[internal] final case class OpenTag(name: String, attr: Option[String] = None) extends Token

private[internal] final case class CloseTag(name: String) extends Token

private[internal] case object NewLine extends Token

private[internal] final case class Text(value: String) extends Token
