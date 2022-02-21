package com.github.gchudnov.swearwolf.rich.internal.parser

import scala.annotation.tailrec

// Re, Ae, T1, T2, Be, T3, Bx, T4, Ax, Ce, T5, Cx, Rx

object Parser:
  def parse(input: String): Either[Throwable, Seq[Element]] =  {
    @tailrec
    def iterate(acc: Stack[Element], tokens: Seq[Token]): Either[Throwable, Seq[Element]] = {
      tokens match {
        case Nil =>
          if (acc.size == 1) then 
            val (t, _) = acc.pop()
            val te = t.asInstanceOf[TagElement]
            Right(te.children)
          else 
            Left(new ParserException("Unbalanced tags"))
        case head +: tail =>
          head match {
            case t @ OpenTag(name, attr) =>
              val te = TagElement.empty(name)
              iterate(acc.push(t), tail)
              ???
            case t @ CloseTag(name) =>
              ???
            case t @ Text(text) =>
              iterate(acc.push(t), tail)
          }
      }
    }

    val tokens = Lexer.lex(input)
    iterate(NonEmptyStack(TagElement.empty("root")), tokens)
  }
