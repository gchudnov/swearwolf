package com.github.gchudnov.swearwolf.util.strings

import scala.annotation.tailrec

object Strings:

  extension (s: String)
    /**
     * Clip string to the given size.
     */
    def clip(width: Int): String =
      if s.length > width then s.substring(0, width)
      else s

    /**
     * Pad string to the given width on the right side.
     */
    def padRight(width: Int): String =
      if s.length < width then s.concat(" ".repeat(width - s.length))
      else s

    /**
     * Pad string to the given width on the left side.
     */
    def padLeft(width: Int): String =
      if s.length < width then " ".repeat(width - s.length).concat(s)
      else s

    /**
     * Removes non-printable characters from the string
     */
    def sanitize(): String =
      if s.exists(_ < 32) then s.map(ch => if ch >= 32 then ch else '?')
      else s

    /**
     * Wraps the text to fit the given width, producing more lines
     */
    def wrap(width: Int): List[String] =
      val sepRx = "\\s"
      val sep   = " "

      def len(ls: Vector[String]): Int =
        val sz = ls.map(_.length).sum
        val ps = if ls.nonEmpty then ls.size - 1 else 0
        sz + ps

      type LineState = (Vector[Vector[String]], Vector[String])

      @tailrec
      def accumulate(acc: LineState, word: String): LineState =
        if word.isEmpty then acc
        else
          val (completeLines, curLine) = acc

          val usedWidth      = len(curLine)
          val availableWidth = if usedWidth == 0 then width else width - sep.length - usedWidth

          if availableWidth > 0 && (word.length <= availableWidth || word.length > width) then
            val (headWord, restWord) = word.splitAt(availableWidth)
            accumulate((completeLines, curLine :+ headWord), restWord)
          else accumulate((completeLines :+ curLine, Vector.empty[String]), word)

      val initialState = (Vector.empty[Vector[String]], Vector.empty[String])

      val (lines, line) = s
        .split(sepRx)
        .foldLeft(initialState)(accumulate)

      val allLines = lines :+ line

      allLines
        .filter(_.nonEmpty)
        .map(_.filter(_.nonEmpty).mkString(sep))
        .toList

    /**
     * Add ellipsis on the left if text doesn't fit the width
     */
    def maybeEllipsisLeft(width: Int): String =
      maybeEllipsis(width, isLeft = true)

    /**
     * Add ellipsis on the right if text doesn't fit the width
     */
    def maybeEllipsisRight(width: Int): String =
      maybeEllipsis(width, isLeft = false)

    private def maybeEllipsis(width: Int, isLeft: Boolean): String =
      if s.length <= width then s
      else
        val fill = "..."
        val (i0, i1) =
          if isLeft then (s.length - width + fill.length, s.length)
          else (0, width - fill.length)

        val clipped = s.substring(i0, i1)

        if isLeft then fill + clipped else clipped + fill

    /**
     * Add ellipsis on the left
     */
    def forceEllipsisLeft(): String =
      forceEllipsis(isLeft = true)

    /**
     * Add ellipsis on the right
     */
    def forceEllipsisRight(): String =
      forceEllipsis(isLeft = false)

    private def forceEllipsis(isLeft: Boolean): String =
      val fill = "..."
      if isLeft then fill ++ s.substring(fill.length)
      else s.substring(0, s.length - fill.length) + fill
