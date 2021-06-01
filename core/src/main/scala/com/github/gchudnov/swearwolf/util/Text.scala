package com.github.gchudnov.swearwolf.util

import scala.annotation.tailrec

object Text {

  /**
   * Clip string to the given size.
   * @param width
   *   size to clip the string to
   * @param value
   *   string value
   * @return
   *   clipped string
   */
  def clip(width: Int)(value: String): String =
    if (value.length > width)
      value.substring(0, width)
    else
      value

  /**
   * Pad string to the given width on the right side.
   * @param width
   *   width of the area
   * @param value
   *   string value
   * @return
   *   padded string
   */
  def padRight(width: Int)(value: String): String =
    if (value.length < width)
      value.concat(" ".repeat(width - value.length))
    else
      value

  /**
   * Pad string to the given width on the left side.
   * @param width
   *   width of the area
   * @param value
   *   string value
   * @return
   *   padded string
   */
  def padLeft(width: Int)(value: String): String =
    if (value.length < width)
      " ".repeat(width - value.length).concat(value)
    else
      value

  /**
   * Removes non-printable characters from the string
   * @param value
   *   String
   * @return
   *   Possibly modified string
   */
  def sanitize(value: String): String =
    if (value.exists(_ < 32))
      value.map(ch => if (ch >= 32) ch else '?')
    else
      value

  /**
   * Wraps the text to fit the given width, producing more lines
   * @param width
   *   available width
   * @param value
   *   string value to wrap
   * @return
   *   a collection of strings.
   */
  def wrap(width: Int)(value: String): List[String] = {
    val sepRx = "\\s"
    val sep   = " "

    def len(ls: Vector[String]): Int = {
      val sz = ls.map(_.length).sum
      val ps = if (ls.nonEmpty) ls.size - 1 else 0
      sz + ps
    }

    type LineState = (Vector[Vector[String]], Vector[String])

    @tailrec
    def accumulate(acc: LineState, word: String): LineState =
      if (word.isEmpty) {
        acc
      } else {
        val (completeLines, curLine) = acc

        val usedWidth      = len(curLine)
        val availableWidth = if (usedWidth == 0) width else width - sep.length - usedWidth

        if (availableWidth > 0 && (word.length <= availableWidth || word.length > width)) {
          val (headWord, restWord) = word.splitAt(availableWidth)
          accumulate((completeLines, curLine :+ headWord), restWord)
        } else {
          accumulate((completeLines :+ curLine, Vector.empty[String]), word)
        }
      }

    val initialState = (Vector.empty[Vector[String]], Vector.empty[String])

    val (lines, line) = value
      .split(sepRx)
      .foldLeft(initialState)(accumulate)

    val allLines = lines :+ line

    allLines
      .filter(_.nonEmpty)
      .map(_.filter(_.nonEmpty).mkString(sep))
      .toList
  }

  /**
   * Add ellipsis on the left if text doesn't fit the width
   */
  def maybeEllipsisLeft(width: Int)(value: String): String =
    maybeEllipsis(width)(value, isLeft = true)

  /**
   * Add ellipsis on the right if text doesn't fit the width
   */
  def maybeEllipsisRight(width: Int)(value: String): String =
    maybeEllipsis(width)(value, isLeft = false)

  private def maybeEllipsis(width: Int)(value: String, isLeft: Boolean): String =
    if (value.length <= width)
      value
    else {
      val fill = "..."
      val (i0, i1) =
        if (isLeft)
          (value.length - width + fill.length, value.length)
        else
          (0, width - fill.length)

      val clipped = value.substring(i0, i1)

      if (isLeft) fill + clipped else clipped + fill
    }

  /**
   * Add ellipsis on the left
   */
  def forceEllipsisLeft(value: String): String =
    forceEllipsis(value, isLeft = true)

  /**
   * Add ellipsis on the right
   */
  def forceEllipsisRight(value: String): String =
    forceEllipsis(value, isLeft = false)

  private def forceEllipsis(value: String, isLeft: Boolean): String = {
    val fill = "..."
    if (isLeft)
      fill ++ value.substring(fill.length)
    else
      value.substring(0, value.length - fill.length) + fill
  }
}
