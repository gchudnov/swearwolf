package com.github.gchudnov.swearwolf.rich.internal

import scala.annotation.tailrec
import com.github.gchudnov.swearwolf.util.data.*
import scala.util.control.Exception.allCatch
import com.github.gchudnov.swearwolf.util.func.MonadError

import scala.collection.immutable.Seq

private[rich] object Parser:
  def parse[F[_]: MonadError](input: String): F[Seq[Element]] =
    @tailrec
    def iterate(acc: Stack[TagElement], tokens: Seq[Token]): Seq[Element] =
      tokens match
        case Nil =>
          if acc.size == 1 then
            val (x, _) = acc.pop()
            x.children
          else throw new ParserException(s"Unbalanced tags: Close tag for '${acc.top.name}' is missing")

        case head +: tail =>
          head match
            case OpenTag(name, attr) =>
              iterate(acc.push(TagElement(name, attr, Nil)), tail)

            case CloseTag(name) =>
              if acc.isEmpty then throw new ParserException(s"Unbalanced tags: Open tag for '$name' is missing")
              val (x, xs) = acc.pop()
              if x.name != name then throw new ParserException(s"Unbalanced tags: Open tag for '${x.name}', but Close tag for '${name}'")
              if xs.isEmpty then throw new ParserException(s"Unbalanced tags: Open tag is missing")
              val (y, ys) = xs.pop()
              val y1      = y.copy(children = y.children :+ x)
              iterate(ys.push(y1), tail)

            case NewLine =>
              val nl      = NewLineElement
              val (x, xs) = acc.pop()
              val x1      = x.copy(children = x.children :+ nl)
              iterate(xs.push(x1), tail)

            case Text(text) =>
              val txt     = TextElement(text)
              val (x, xs) = acc.pop()
              val x1      = x.copy(children = x.children :+ txt)
              iterate(xs.push(x1), tail)

    val tokens = Lexer.lex(input)
    summon[MonadError[F]].attempt(iterate(NonEmptyStack(TagElement.empty("#root")), tokens))
