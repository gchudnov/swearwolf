package com.github.gchudnov.swearwolf.term.internal

import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.spans.ByteSpan
import com.github.gchudnov.swearwolf.util.data.Stack
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import com.github.gchudnov.swearwolf.term.EscSeq

/**
 * Transforms Span to an array of Bytes that can be sent to the terminal.
 */
object SpanCompiler:

  private sealed trait Switch
  private case object On  extends Switch
  private case object Off extends Switch

  /**
   * {{{{ Option[Switch]: None: no changes to the effect Some(On): turn on the effect of the span Some(Off): turn off the effect of the span
   *
   * (Option[Color], Option[Color]) (None, None): no changes to the color (Some(c), None): change the color from c to none (None, Some(c)): change the color from none to c
   * (Some(c1), Some(c2)): change the color from c1 to c2 }}}
   */
  private case class StateDiff(
    empty: Option[Switch] = None,
    fgColor: (Option[Color], Option[Color]) = (None, None),
    bgColor: (Option[Color], Option[Color]) = (None, None),
    bold: Option[Switch] = None,
    italic: Option[Switch] = None,
    underline: Option[Switch] = None,
    blink: Option[Switch] = None,
    invert: Option[Switch] = None,
    strikethrough: Option[Switch] = None,
    transparent: Option[Switch] = None,
    noColor: Option[Switch] = None
  )

  private case class State(
    empty: Int = 0,
    fgColor: Stack[Color] = Stack.empty[Color],
    bgColor: Stack[Color] = Stack.empty[Color],
    bold: Int = 0,
    italic: Int = 0,
    underline: Int = 0,
    blink: Int = 0,
    invert: Int = 0,
    strikethrough: Int = 0,
    transparent: Int = 0,
    noColor: Int = 0
  ):
    def withStyle(style: TextStyle): State = style match
      case TextStyle.Empty             => copy(empty = empty + 1)
      case TextStyle.Bold              => copy(bold = bold + 1)
      case TextStyle.Italic            => copy(italic = italic + 1)
      case TextStyle.Underline         => copy(underline = underline + 1)
      case TextStyle.Blink             => copy(blink = blink + 1)
      case TextStyle.Invert            => copy(invert = invert + 1)
      case TextStyle.Strikethrough     => copy(strikethrough = strikethrough + 1)
      case TextStyle.Transparent       => copy(transparent = transparent + 1)
      case TextStyle.NoColor           => copy(noColor = noColor + 1)
      case TextStyle.Foreground(color) => copy(fgColor = fgColor.push(color))
      case TextStyle.Background(color) => copy(bgColor = bgColor.push(color))
      case TextStyleSeq(styles)        => styles.foldLeft(this)(_.withStyle(_))

    def diff(that: State): StateDiff =
      StateDiff(
        empty = intDiff(empty, that.empty),
        fgColor = colorDiff(fgColor, that.fgColor),
        bgColor = colorDiff(bgColor, that.bgColor),
        bold = intDiff(bold, that.bold),
        italic = intDiff(italic, that.italic),
        underline = intDiff(underline, that.underline),
        blink = intDiff(blink, that.blink),
        invert = intDiff(invert, that.invert),
        strikethrough = intDiff(strikethrough, that.strikethrough),
        transparent = intDiff(transparent, that.transparent),
        noColor = intDiff(noColor, that.noColor)
      )

    private def intDiff(lhs: Int, rhs: Int): Option[Switch] =
      if (rhs == lhs) then None else if (rhs > lhs) then Some(On) else Some(Off)

    private def colorDiff(lhs: Stack[Color], rhs: Stack[Color]): (Option[Color], Option[Color]) =
      (lhs.isEmpty, rhs.isEmpty) match
        case (true, true)   => (None, None)
        case (false, true)  => (Some(lhs.top), None)
        case (true, false)  => (None, Some(rhs.top))
        case (false, false) => if (rhs.top == lhs.top) then (None, None) else (Some(lhs.top), Some(rhs.top))

  private object State:
    val empty: State =
      State()

  def compile(span: Span): Bytes =

    def iterate(state: State, span: Span): Bytes = span match
      case StyleSpan(style, children) =>
        val newState               = state.withStyle(style)
        val diff                   = state.diff(newState)
        val (setBytes, resetBytes) = diffToBytes(diff)

        val prefix = Bytes(setBytes)
        val suffix = Bytes(resetBytes)

        val childrenAcc = children.foldLeft(Bytes.empty) { case (acc, ch) => acc + iterate(newState, ch) }
        val newAcc = prefix + childrenAcc + suffix

        newAcc

      case TextSpan(text) =>
        Bytes(text.getBytes)

      case ByteSpan(bytes) =>
        Bytes(bytes)

    iterate(State.empty, span)

  /**
   * Returns a pair that can be used to (SET, UNSET) the diff.
   */
  private def diffToBytes(diff: StateDiff): (Array[Byte], Array[Byte]) =
    val emptyTuple = (Array.empty[Byte], Array.empty[Byte])

    val (setEmpty, resetEmpty)                 = emptyTuple
    val (setFgColor, resetFgColor)             = colorDiffToBytes(diff.fgColor)
    val (setBgColor, resetBgColor)             = colorDiffToBytes(diff.bgColor)
    val (setBold, resetBold)                   = switchDiffToBytes(diff.bold, EscSeq.bold.bytes, EscSeq.resetBold.bytes)
    val (setItalic, resetItalic)               = switchDiffToBytes(diff.italic, EscSeq.italic.bytes, EscSeq.resetItalic.bytes)
    val (setUnderline, resetUnderline)         = switchDiffToBytes(diff.underline, EscSeq.underline.bytes, EscSeq.resetUnderline.bytes)
    val (setBlink, resetBlink)                 = switchDiffToBytes(diff.blink, EscSeq.blink.bytes, EscSeq.resetBlink.bytes)
    val (setInvert, resetInvert)               = switchDiffToBytes(diff.invert, EscSeq.invert.bytes, EscSeq.resetInvert.bytes)
    val (setStrikethrough, resetStrikethrough) = switchDiffToBytes(diff.strikethrough, EscSeq.strikethrough.bytes, EscSeq.resetStrikethrough.bytes)
    val (setTransparent, resetTransparent)     = emptyTuple
    val (setNoColor, resetNoColor)             = emptyTuple

    val setBytes = setEmpty ++ setFgColor ++ setBgColor ++ setBold ++ setItalic ++ setUnderline ++ setBlink ++ setInvert ++ setStrikethrough ++ setTransparent ++ setNoColor
    val resetBytes =
      resetEmpty ++ resetFgColor ++ resetBgColor ++ resetBold ++ resetItalic ++ resetUnderline ++ resetBlink ++ resetInvert ++ resetStrikethrough ++ resetTransparent ++ resetNoColor

    (setBytes, resetBytes)

  /**
   * Returns a pair that can be used to (SET, UNSET) the diff.
   */
  private def colorDiffToBytes(diff: (Option[Color], Option[Color])): (Array[Byte], Array[Byte]) =
    diff match
      case (None, None)         => (Array.empty[Byte], Array.empty[Byte])
      case (None, Some(c1))     => (EscSeq.foreground(c1).bytes, EscSeq.resetForeground.bytes)
      case (Some(c0), None)     => (EscSeq.resetForeground.bytes, EscSeq.foreground(c0).bytes)
      case (Some(c0), Some(c1)) => (EscSeq.foreground(c1).bytes, EscSeq.foreground(c0).bytes)

  /**
   * Returns a pair that can be used to (SET, UNSET) the diff.
   */
  private def switchDiffToBytes(diff: Option[Switch], on: => Array[Byte], off: => Array[Byte]): (Array[Byte], Array[Byte]) =
    diff match
      case None      => (Array.empty[Byte], Array.empty[Byte])
      case Some(On)  => (on, off)
      case Some(Off) => (off, on)
