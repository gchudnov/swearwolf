package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term._
import com.github.gchudnov.swearwolf.util.Size

import scala.annotation.tailrec

/**
 * Reads an escape sequence.
 *
 * Up:
 *   1b  5b 41
 *   ESC [  A
 *
 * Alt-Up:
 *
 *   Esc [ 1 ; 3 A
 *
 * F1:
 *   1b 4f 50
 *   Esc O P
 *
 * F5:
 *
 * Ctrl-Shift-F6:
 *   Esc [ 1 7 ; 6 R
 *
 * Size-Report:
 *   1b  5b 38 3b 35 33 3b 32 30 34 74
 *   ESC [  8  ;  5  3  ;  2  0  4  t
 *               height     width
 */
object EscReader extends BasicKeySeqReader {

  trait State

  case object Start   extends State
  case object Bracket extends State
  case object Num1    extends State
  case object Num2    extends State
  case object Num3    extends State
  case object Finish  extends State

  private val lastMap: Map[Byte, KeyCode] = Map(
    'A'.toByte -> KeyCode.Up,
    'B'.toByte -> KeyCode.Down,
    'C'.toByte -> KeyCode.Right,
    'D'.toByte -> KeyCode.Left,
    'F'.toByte -> KeyCode.End,
    'H'.toByte -> KeyCode.Home,
    'P'.toByte -> KeyCode.F1,
    'Q'.toByte -> KeyCode.F2,
    'R'.toByte -> KeyCode.F3,
    'S'.toByte -> KeyCode.F4
  )

  private val stdMap: Map[Int, KeyCode] = Map(
    1  -> KeyCode.Home,
    2  -> KeyCode.Insert,
    3  -> KeyCode.Delete,
    4  -> KeyCode.End,
    5  -> KeyCode.PageUp,
    6  -> KeyCode.PageDown,
    11 -> KeyCode.F1,
    12 -> KeyCode.F2,
    13 -> KeyCode.F3,
    14 -> KeyCode.F4,
    15 -> KeyCode.F5,
    16 -> KeyCode.F5,
    17 -> KeyCode.F6,
    18 -> KeyCode.F7,
    19 -> KeyCode.F8,
    20 -> KeyCode.F9,
    21 -> KeyCode.F10,
    23 -> KeyCode.F11,
    24 -> KeyCode.F12
  )

  override def read(data: Seq[Byte]): (KeySeq, Seq[Byte]) = {

    @tailrec
    def iterate(state: State, num1: Int, num2: Int, num3: Int, last: Byte, xs: Seq[Byte]): (KeySeq, Seq[Byte]) =
      state match {
        case Start =>
          xs match {
            case x +: xt if isEsc(x) =>
              iterate(Bracket, num1, num2, num3, last, xt)
            case _ =>
              (UnknownKeySeq, data)
          }

        case Bracket =>
          xs match {
            case x +: xt if isBracket(x) =>
              iterate(Num1, num1, num2, num3, last, xt)
            case x +: xt =>
              (UnknownKeySeq, data)
            case _ =>
              (PartialKeySeq, data)
          }

        case Num1 =>
          xs match {
            case x +: xt if isSemicolon(x) =>
              iterate(Num2, num1, num2, num3, last, xt)
            case x +: xt if isDigit(x) =>
              iterate(Num1, appendNumber(num1, x), num2, num3, last, xt)
            case x +: xt =>
              iterate(Finish, num1, num2, num3, x, xt)
            case _ =>
              (PartialKeySeq, data)
          }

        case Num2 =>
          xs match {
            case x +: xt if isSemicolon(x) =>
              iterate(Num3, num1, num2, num3, last, xt)
            case x +: xt if isDigit(x) =>
              iterate(Num2, num1, appendNumber(num2, x), num3, last, xt)
            case x +: xt =>
              iterate(Finish, num1, num2, num3, x, xt)
            case _ =>
              (PartialKeySeq, data)
          }

        case Num3 =>
          xs match {
            case x +: xt if isDigit(x) =>
              iterate(Num3, num1, num2, appendNumber(num3, x), last, xt)
            case x +: xt =>
              iterate(Finish, num1, num2, num3, x, xt)
            case _ =>
              (PartialKeySeq, data)
          }

        case Finish =>
          toResult(num1, num2, num3, last, xs)
      }

    iterate(Start, num1 = 0, num2 = 0, num3 = 0, last = 0, data)
  }

  private def toResult(num1: Int, num2: Int, num3: Int, last: Byte, rest: Seq[Byte]): (KeySeq, Seq[Byte]) =
    if (isLowerT(last) && num1 == 8)
      (SizeKeySeq(Size(width = num3, height = num2)), rest)
    else if (isTilde(last) && stdMap.contains(num1)) {
      val key  = stdMap(num1)
      val mods = toModifiers(num2)
      (CtrlKeySeq(key, mods), rest)
    } else if (lastMap.contains(last)) {
      val key  = lastMap(last)
      val mods = toModifiers(num1)
      (CtrlKeySeq(key, mods), rest)
    } else
      (UnknownKeySeq, rest)

  private[term] def toModifiers(n: Int): Set[KeyModifier] =
    if (n == 0 || n == 1)
      Set.empty[KeyModifier]
    else
      modMap.foldLeft(Set.empty[KeyModifier]) { case (acc, (mod, km)) =>
        if (((n - 1) & mod) > 0)
          acc + km
        else
          acc
      }
}
