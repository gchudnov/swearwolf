package com.github.gchudnov.swearwolf.rich.internal.parser

import scala.annotation.tailrec

object Parser:
  def parse(input: String): Seq[Element] =  {
    @tailrec
    def iterate(tokens: Seq[Token], stack: Stack[Token]): Seq[Element] = {

      // Ae, T1, T2, Be, T3, Bx, T4, Ax

      def rewind(): Seq[Element] = {
        ???
      }

      tokens match {
        case Nil =>
          ???
        case head +: tail =>
          head match {
            case t @ OpenTag(name, attr) =>
              iterate(tail, stack.push(t))
            case t @ CloseTag(name) =>
              ???
            case t @ Text(text) =>
              iterate(tail, stack.push(t))
          }
      }
    }

    val tokens = Lexer.lex(input)
    iterate(tokens, EmptyStack)
  }
