package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.KeyModifier

import scala.util.control.Exception.nonFatalCatch

private[readers] abstract class BasicKeySeqReader extends KeySeqReader {

  val modMap: Map[Int, KeyModifier] = Map(
    1 -> KeyModifier.Shift,
    2 -> KeyModifier.Alt,
    4 -> KeyModifier.Ctrl
  )

  def appendNumber(acc: Int, x: Byte): Int =
    acc * 10 + Character.digit(x.toInt, 10)

  def isDigit(value: Byte): Boolean =
    Character.isDigit(value.toInt)

  def isControl(value: Byte): Boolean =
    Character.isISOControl(value.toChar)

  def isPrintable(value: Byte): Boolean =
    if (isControl(value))
      false
    else
      nonFatalCatch
        .either(Character.UnicodeBlock.of(value.toInt))
        .flatMap(Option(_).toRight(new RuntimeException(s"${value} is not a member of a unicode block")))
        .forall(_ != Character.UnicodeBlock.SPECIALS)

  // '<'
  def isLessThan(value: Byte): Boolean =
    value == 0x3c.toByte

  // '[' or 'O'
  def isBracket(value: Byte): Boolean =
    value == 0x5b.toByte || value == 0x4f.toByte

  // ;
  def isSemicolon(value: Byte): Boolean =
    value == 0x3b.toByte

  // t
  def isLowerT(value: Byte): Boolean =
    value == 0x74.toByte

  // M
  def isUpperM(value: Byte): Boolean =
    value == 0x4d.toByte

  // m
  def isLowerM(value: Byte): Boolean =
    value == 0x6d.toByte

  // ~
  def isTilde(value: Byte): Boolean =
    value == 0x7e.toByte

  // {ESC}
  def isEsc(value: Byte): Boolean =
    value == 0x1b.toByte

  // <- (Backspace)
  def isBackspace(value: Byte): Boolean =
    value == 0x7f.toByte || value == 0x08.toByte

  // \r \n
  def isEnter(value: Byte): Boolean =
    value == 0x0a || value == 0x0d

  // \t
  def isTab(value: Byte): Boolean =
    value == 0x09.toByte
}
