package com.github.gchudnov.swearwolf.term.internal.screens

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.internal.spans.SpanCompiler
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle

/**
 * Screen that is backed by the array. All strings that are written to it are stored without styles.
 * @param szScreen
 *   size of the screen.
 * @param cellChar
 *   character to put in 1 cell.
 * @param borderChar
 *   character to use on the right-side of the screen as a delimiter.
 */
private[screens] final class ArrayScreen(szScreen: Size, cellChar: Char, borderChar: Option[Char]) extends BasicScreen:
  import ArrayScreen.*

  private var view: Array[Array[Char]] = blankArray(szScreen, cellChar)

  override def toString: String =
    view
      .map(_.mkString + borderChar.getOrElse(""))
      .mkString(sys.props("line.separator"))

  override def size: Size = szScreen

  override def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit] =
    putText(pt, viewText(value))

  override def eventLoop(handler: KeySeqHandler): Either[Throwable, Unit] = Right(())

  override def eventPoll(): Either[Throwable, List[KeySeq]] = Right(List.empty[KeySeq])

  override def cursorHide(): Either[Throwable, Unit] = Right(())

  override def cursorShow(): Either[Throwable, Unit] = Right(())

  override def mouseTrack(): Either[Throwable, Unit] = Right(())

  override def mouseUntrack(): Either[Throwable, Unit] = Right(())

  override def bufferNormal(): Either[Throwable, Unit] = Right(())

  override def bufferAlt(): Either[Throwable, Unit] = Right(())

  override def clear(): Either[Throwable, Unit] = Right({
    view = blankArray(szScreen, cellChar)
  })

  override def flush(): Either[Throwable, Unit] = Right(())

  override def close(): Unit = ()

  private def putText(pt: Point, value: String): Either[Throwable, Unit] =
    if pt.y >= szScreen.height || pt.y < 0 then Right(())
    else
      val oldLine = view(pt.y).mkString

      val first = oldLine.substring(0, Math.min(pt.x, oldLine.length))
      val last  = oldLine.substring(Math.min(pt.x + value.length, oldLine.length))

      val newLine = (first + value + last).take(szScreen.width)

      require(oldLine.length == newLine.length, "new-line length should be equal to old-line length")

      view = view.updated(pt.y, newLine.toCharArray)
      Right(())

private[term] object ArrayScreen:

  private[term] val DefaultCellChar   = '.'
  private[term] val DefaultBorderChar = '|'

  def make(size: Size, cellChar: Char = DefaultCellChar, borderChar: Option[Char] = Some(DefaultBorderChar)): ArrayScreen =
    new ArrayScreen(size, cellChar, borderChar)

  def compile(span: Span): Bytes =
    SpanCompiler.compile(span)

  private def blankArray(sz: Size, ch: Char): Array[Array[Char]] =
    Array.fill(sz.height, sz.width)(ch)

  /**
   * View Text without Escape or Control Character
   */
  private[screens] def viewText(bytes: Array[Byte]): String =
    val rxEsc = s"""${EscSeq.escChar}\\[[\\d;]+\\w"""
    val rxCtl = "\\p{C}"

    val str   = Bytes(bytes).asString
    val parts = str.split(rxEsc)

    parts.filter(_.nonEmpty).mkString.replaceAll(rxCtl, "");
