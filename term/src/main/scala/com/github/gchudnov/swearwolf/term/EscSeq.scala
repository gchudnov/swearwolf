package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.geometry.Point

/**
 * Escape sequences
 *
 * https://en.wikipedia.org/wiki/ANSI_escape_code
 *
 * http://rtfm.etla.org/xterm/ctlseq.html
 */
final case class EscSeq(value: String):
  def bytes: Array[Byte] = value.getBytes()

object EscSeq:
  val escChar: Char = '\u001b'

  private def esc(data: String): EscSeq =
    new EscSeq(s"${escChar}${data}")

  // CSI, "Control Sequence Introducer"
  private def csi(data: String): EscSeq =
    esc(s"[$data")

  // SGR, Select Graphic Rendition
  private def sgr(code: Int, ps: List[Int] = List.empty[Int]): EscSeq = csi(s"${(code :: ps).mkString(";")}m")

  // CUP, Cursor Position
  private def cup(row: Int, col: Int): EscSeq = csi(s"$row;${col}H")

  // CUU, Cursor Up
  private def cuu(offset: Int): EscSeq = csi(s"${offset}A")

  // CUD, Cursor Down
  private def cud(offset: Int): EscSeq = csi(s"${offset}B")

  // CUF, Cursor Forward
  private def cuf(offset: Int): EscSeq = csi(s"${offset}C")

  // CUB, Cursor Back
  private def cub(offset: Int): EscSeq = csi(s"${offset}D")

  // ED, Erase in Display
  private def ed(): EscSeq = csi("2J")

  // empty esc-sequence, used as a placeholder
  val empty: EscSeq = new EscSeq("")

  // screen
  val status: EscSeq       = csi("5n")
  val erase: EscSeq        = ed()
  val altBuffer: EscSeq    = csi("?47;h")
  val normalBuffer: EscSeq = csi("?47;l")
  val textAreaSize: EscSeq = csi("18t")

  // colors
  def foreground(c: Color): EscSeq = sgr(38, List(2, c.r & 0xff, c.g & 0xff, c.b & 0xff))
  val resetForeground: EscSeq      = sgr(39)
  def background(c: Color): EscSeq = sgr(48, List(2, c.r & 0xff, c.g & 0xff, c.b & 0xff))
  val resetBackground: EscSeq      = sgr(49)
  val bold: EscSeq                 = sgr(1)
  val resetBold: EscSeq            = sgr(22)
  val underline: EscSeq            = sgr(4)
  val resetUnderline: EscSeq       = sgr(24)
  val italic: EscSeq               = sgr(3)
  val resetItalic: EscSeq          = sgr(23)
  val strikethrough: EscSeq        = sgr(9)
  val resetStrikethrough: EscSeq   = sgr(29)
  val blink: EscSeq                = sgr(5)
  val resetBlink: EscSeq           = sgr(25)
  val invert: EscSeq               = sgr(7)
  val resetInvert: EscSeq          = sgr(27)
  val reset: EscSeq                = sgr(0)

  // cursor ; 1-based
  def cursorPosition(pt: Point): EscSeq = cup(row = pt.y + 1, col = pt.x + 1)
  val cursorShow: EscSeq                = csi("?25;h")
  val cursorHide: EscSeq                = csi("?25;l")
  val cursorSave: EscSeq                = esc("7")
  val cursorRestore: EscSeq             = esc("8")
  def cursorUp(offset: Int): EscSeq     = cuu(offset)
  def cursorDown(offset: Int): EscSeq   = cud(offset)
  def cursorRight(offset: Int): EscSeq  = cuf(offset)
  def cursorLeft(offset: Int): EscSeq   = cub(offset)

  // mouse
  val mouseTracking: EscSeq      = csi("?1002;1006h")
  val resetMouseTracking: EscSeq = csi("?1002;1006l")
