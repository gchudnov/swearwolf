package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term.{ ParsedReadState, PartialReadState, ReadState, UnknownReadState }
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.{ KeyModifier, MouseAction, MouseButton, MouseKeySeq }

import scala.annotation.tailrec

/**
 * Read Mouse Events
 *
 * https://invisible-island.net/xterm/ctlseqs/ctlseqs.html
 *
 * https://invisible-island.net/xterm/ctlseqs/ctlseqs.pdf
 *
 * https://upload.wikimedia.org/wikipedia/commons/d/dd/ASCII-Table.svg
 *
 * {{{
 *
 * SGR 1006 example: "\e[<0;15;240M"
 * mouse release has trailing 'm' rather than 'M'.
 *
 * Mouse event: "\E[M"
 *
 * Middle Mouse Key Press:
 * 1b    5b 3c 31 3b 39 36 3b 31 33 4d
 *
 * Right Mouse Key Press:
 * 1b    5b 3c 32 3b 35 30 3b 31 39 4d
 *
 * Left Mouse Key Press:
 * 1b    5b 3c 30 3b 39 32 3b 33 33 4d
 * {ESC} [  <  0  ;  9  2  ;  3  3  M
 *
 * Left Mouse Key Release:
 * 1b    5b 3c 30 3b 37 35 3b 31 39 6d
 * {ESC} [  <  0  ;         ;       m
 *
 * }}}
 */
private[term] object MouseReader extends BasicKeySeqReader:

  sealed trait State

  case object Start    extends State
  case object Bracket  extends State
  case object LessThan extends State
  case object Num1     extends State
  case object Num2     extends State
  case object Num3     extends State
  case object Finish   extends State

  private val buttonMap: Map[Int, MouseButton] = Map(
    0  -> MouseButton.Left,
    1  -> MouseButton.Middle,
    2  -> MouseButton.Right,
    64 -> MouseButton.ScrollBackward,
    65 -> MouseButton.ScrollForward
  )

  override def read(data: Seq[Byte]): ReadState =

    @tailrec
    def iterate(state: State, num1: Int, num2: Int, num3: Int, last: Byte, xs: Seq[Byte]): ReadState =
      state match
        case Start =>
          xs match
            case x +: xt if isEsc(x) =>
              iterate(Bracket, num1, num2, num3, last, xt)
            case _ =>
              UnknownReadState(data)

        case Bracket =>
          xs match
            case x +: xt if isBracket(x) =>
              iterate(LessThan, num1, num2, num3, last, xt)
            case x +: xt =>
              UnknownReadState(data)
            case _ =>
              PartialReadState(data)

        case LessThan =>
          xs match
            case x +: xt if isLessThan(x) =>
              iterate(Num1, num1, num2, num3, last, xt)
            case x +: xt =>
              UnknownReadState(data)
            case _ =>
              PartialReadState(data)

        case Num1 =>
          xs match
            case x +: xt if isSemicolon(x) =>
              iterate(Num2, num1, num2, num3, last, xt)
            case x +: xt if isDigit(x) =>
              iterate(Num1, appendNumber(num1, x), num2, num3, last, xt)
            case x +: xt =>
              iterate(Finish, num1, num2, num3, x, xt)
            case _ =>
              PartialReadState(data)

        case Num2 =>
          xs match
            case x +: xt if isSemicolon(x) =>
              iterate(Num3, num1, num2, num3, last, xt)
            case x +: xt if isDigit(x) =>
              iterate(Num2, num1, appendNumber(num2, x), num3, last, xt)
            case x +: xt =>
              iterate(Finish, num1, num2, num3, x, xt)
            case _ =>
              PartialReadState(data)

        case Num3 =>
          xs match
            case x +: xt if isDigit(x) =>
              iterate(Num3, num1, num2, appendNumber(num3, x), last, xt)
            case x +: xt =>
              iterate(Finish, num1, num2, num3, x, xt)
            case _ =>
              PartialReadState(data)

        case Finish =>
          toResult(num1, num2, num3, last, xs)

    iterate(Start, num1 = 0, num2 = 0, num3 = 0, last = 0, data)

  private def toResult(num1: Int, num2: Int, num3: Int, last: Byte, rest: Seq[Byte]): ReadState =
    if isUpperM(last) then
      // press
      ParsedReadState(MouseKeySeq(toPoint(num2, num3), toMouseButton(num1), MouseAction.Press, toModifiers(num1)), rest)
    else if isLowerM(last) then
      // release
      ParsedReadState(MouseKeySeq(toPoint(num2, num3), toMouseButton(num1), MouseAction.Release, toModifiers(num1)), rest)
    else UnknownReadState(rest)

  private def toPoint(x: Int, y: Int): Point =
    Point(toPos(x), toPos(y))

  private def toPos(n: Int): Int =
    if n > 0 then (n - 1) else 0

  private def toMouseButton(n: Int): MouseButton =
    val m = n & 67 // (bits: 1000011)
    buttonMap(m)

  private[term] def toModifiers(n: Int): Set[KeyModifier] =
    val m = (n >> 2) & 7 // mask only modifiers bits
    if n == 0 then Set.empty[KeyModifier]
    else
      modMap.foldLeft(Set.empty[KeyModifier]) { case (acc, (mod, km)) =>
        if (m & mod) > 0 then acc + km
        else acc
      }
