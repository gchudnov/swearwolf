package com.github.gchudnov.swearwolf.rich.internal.parser

import scala.annotation.tailrec
import scala.util.control.Exception.allCatch

object Parser:
  def parse(input: String): Either[Throwable, Seq[Element]] =
    @tailrec
    def iterate(acc: Stack[TagElement], tokens: Seq[Token]): Seq[Element] =
      tokens match
        case Nil =>
          if (acc.size == 1) then
            val (x, _) = acc.pop()
            x.children
          else throw new ParserException(s"Unbalanced tags: Close tag for '${acc.top.name}' is missing")

        case head +: tail =>
          head match
            case OpenTag(name, attr) =>
              iterate(acc.push(TagElement.empty(name)), tail)

            case CloseTag(name) =>
              val (x, xs) = acc.pop()
              if (x.name != name) then throw new ParserException(s"Unbalanced tags: Open tag for '${x.name}', but Close tag for '${name}'")
              val (y, ys) = xs.pop()
              val y1      = y.copy(children = y.children :+ x)
              iterate(ys.push(y1), tail)

            case Text(text) =>
              val txt     = TextElement(text)
              val (x, xs) = acc.pop()
              val x1      = x.copy(children = x.children :+ txt)
              iterate(xs.push(x1), tail)

    val tokens = Lexer.lex(input)
    allCatch.either(iterate(NonEmptyStack(TagElement.empty("root")), tokens))
