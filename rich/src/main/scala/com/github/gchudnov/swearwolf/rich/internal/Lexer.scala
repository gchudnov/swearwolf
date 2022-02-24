package com.github.gchudnov.swearwolf.rich.internal

import scala.util.parsing.combinator.*

private[internal] object Lexer:
  def lex(input: String): Seq[Token] =
    Grammar.parse(Grammar.document, input).getOrElse(Nil)

  private object Grammar extends RegexParsers:
    override def skipWhitespace = false

    def document = (closeTag | openTag | newLine | text) *

    def openTag = "<" ~> tagName ~ opt("""\s*=\s*""".r ~> attrValue) <~ ">" ^^ { case tag ~ attr =>
      OpenTag(tag.toLowerCase, attr)
    }

    def closeTag = "<" ~> "/" ~> tagName <~ ">" ^^ { case tag =>
      CloseTag(tag.toLowerCase)
    }

    def newLine = """\r?\n""".r ^^^ NewLine

    def text = ("""[^\<\n\r]+""".r | "<") ^^ { Text(_) }

    def tagName = """[\w\d_-]+""".r

    def attrValue = "'" ~> """[^']+""".r <~ "'" | "\"" ~> """[^"]+""".r <~ "\"" | """[^\>]+""".r
