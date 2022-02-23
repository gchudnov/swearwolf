package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.RichTextException

/**
 * Parses xml-like syntax to blocks and attributes. Only the structure is parsed. Tags are not being processed.
 */
private[rich] object RichTextParser:

  /**
   * Parse styled text and return internal representation, Block, that can be converted for the output.
   */
  def read(input: String): Either[Throwable, Block] =
    val str = s"<root>$input</root>"
    // parse(str, rich(_))
    //   .fold(
    //     (msg, pos, _) => Left[RichTextException, Block](new RichTextException(s"Cannot parse rich-text: '$msg' at position: $pos")),
    //     (t, _) => Right[RichTextException, Block](t)
    //   )
    ???

  // private def space[_: P]: P[Unit] = P(CharsWhileIn(" \t\r\n", 1))
  // private def quote[_: P]: P[Unit] = P("\"" | "'")

  // private def text[_: P]: P[Block] = (!"<" ~/ AnyChar).rep(1).!.map(n => TextBlock(n))

  // private def name[_: P]: P[String] = (!(" " | ">") ~/ AnyChar).rep(1).!

  // private def attrKey[_: P]: P[String]       = P(!(quote | space | "=") ~ AnyChar).rep(1).!
  // private def attrValue[_: P]: P[String]     = P(!(quote | space) ~ AnyChar).rep(1).!
  // private def attribute[_: P]: P[Attr]       = P(attrKey.! ~ "=" ~/ quote ~ attrValue.! ~ quote).map[Attr](it => NamedAttr(it._1, it._2))
  // private def attributes[_: P]: P[Seq[Attr]] = attribute.rep(min = 0, sep = " ".rep(1))

  // private def start[_: P]: P[(String, Seq[Attr])] = P("<" ~/ name.! ~/ space.? ~ attributes ~ space.? ~ ">")
  // private def end[_: P](n: String): P[String]     = P("</" ~/ n.! ~/ ">")

  // private def tag[_: P]: P[Block] =
  //   start.flatMap { case (n, attrs) =>
  //     def ending = end(n)
  //     val x      = P((!ending ~ (tag | text)).rep(0) ~/ ending)
  //     x.map { case (bs, name) => NamedBlockSeq(name, attrs, bs) }
  //   }

  // private def rich[_: P]: P[Block] = tag ~ End

  sealed trait Attr
  final case class NamedAttr(key: String, value: String) extends Attr

  sealed trait Block
  final case class TextBlock(value: String)                                                                              extends Block
  final case class NamedBlockSeq(name: String, attrs: Seq[Attr] = Seq.empty[Attr], inner: Seq[Block] = Seq.empty[Block]) extends Block

  // parse xml tags and attributes from a string
    
// TODO: this and several other files here must be removed