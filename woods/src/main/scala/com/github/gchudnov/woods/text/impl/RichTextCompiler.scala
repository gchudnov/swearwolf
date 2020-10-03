package com.github.gchudnov.woods.text.impl

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.util.Color
import com.github.gchudnov.woods.text.impl.RichTextStyler._

/**
 * Compiles styles to a series of terminal commands.
 */
object RichTextCompiler {

  final case class State(
    fgColor: Vector[Color] = Vector.empty[Color],
    bgColor: Vector[Color] = Vector.empty[Color],
    bold: Vector[Unit] = Vector.empty[Unit],
    italic: Vector[Unit] = Vector.empty[Unit],
    underline: Vector[Unit] = Vector.empty[Unit],
    blink: Vector[Unit] = Vector.empty[Unit],
    invert: Vector[Unit] = Vector.empty[Unit],
    strikethrough: Vector[Unit] = Vector.empty[Unit]
  )

  /**
   * Compiles styles to the series of commands for the terminal.
   */
  def compile(root: RichStyle): Array[Byte] = {

    def nestedStyles(state: State, bs: Seq[RichStyle]): Array[Byte] =
      bs.foldLeft(Array.empty[Byte]) { (acc, b) =>
        acc ++ iterate(Array.empty[Byte], state, b)
      }

    def iterate(acc: Array[Byte], state: State, s: RichStyle): Array[Byte] =
      s match {
        case RichBoxStyle(nested) =>
          val updRes = nestedStyles(state, nested)
          acc ++ updRes

        case RichTextStyle(it) =>
          acc ++ it.getBytes

        case RichForegroundStyle(c, inner) =>
          val prefix    = if (!state.fgColor.lastOption.contains(c)) EscSeq.foreground(c).bytes else Array.empty[Byte]
          val updStyles = state.copy(fgColor = state.fgColor.appended(c))
          val nested    = nestedStyles(updStyles, inner)
          val suffix = (state.fgColor.lastOption match {
            case None => EscSeq.resetForeground
            case Some(prevColor) if prevColor != c =>
              EscSeq.foreground(prevColor)
            case _ =>
              EscSeq.empty
          }).bytes
          acc ++ prefix ++ nested ++ suffix

        case RichBackgroundStyle(c, inner) =>
          val prefix    = if (!state.bgColor.lastOption.contains(c)) EscSeq.background(c).bytes else Array.empty[Byte]
          val updStyles = state.copy(bgColor = state.bgColor.appended(c))
          val nested    = nestedStyles(updStyles, inner)
          val suffix = (state.bgColor.lastOption match {
            case None => EscSeq.resetBackground
            case Some(prevColor) if prevColor != c =>
              EscSeq.background(prevColor)
            case _ =>
              EscSeq.empty
          }).bytes
          acc ++ prefix ++ nested ++ suffix

        case RichBoldStyle(inner) =>
          val prefix    = if (state.bold.isEmpty) EscSeq.bold.bytes else Array.empty[Byte]
          val updStyles = state.copy(bold = state.bold.appended(()))
          val nested    = nestedStyles(updStyles, inner)
          val suffix    = if (state.bold.isEmpty) EscSeq.resetBold.bytes else Array.empty[Byte]
          acc ++ prefix ++ nested ++ suffix

        case RichItalicStyle(inner) =>
          val prefix    = if (state.italic.isEmpty) EscSeq.italic.bytes else Array.empty[Byte]
          val updStyles = state.copy(italic = state.italic.appended(()))
          val nested    = nestedStyles(updStyles, inner)
          val suffix    = if (state.italic.isEmpty) EscSeq.resetItalic.bytes else Array.empty[Byte]
          acc ++ prefix ++ nested ++ suffix

        case RichUnderlineStyle(inner) =>
          val prefix    = if (state.underline.isEmpty) EscSeq.underline.bytes else Array.empty[Byte]
          val updStyles = state.copy(underline = state.underline.appended(()))
          val nested    = nestedStyles(updStyles, inner)
          val suffix    = if (state.underline.isEmpty) EscSeq.resetUnderline.bytes else Array.empty[Byte]
          acc ++ prefix ++ nested ++ suffix

        case RichBlinkStyle(inner) =>
          val prefix    = if (state.blink.isEmpty) EscSeq.blink.bytes else Array.empty[Byte]
          val updStyles = state.copy(blink = state.blink.appended(()))
          val nested    = nestedStyles(updStyles, inner)
          val suffix    = if (state.blink.isEmpty) EscSeq.resetBlink.bytes else Array.empty[Byte]
          acc ++ prefix ++ nested ++ suffix

        case RichInvertStyle(inner) =>
          val prefix    = if (state.invert.isEmpty) EscSeq.invert.bytes else Array.empty[Byte]
          val updStyles = state.copy(invert = state.invert.appended(()))
          val nested    = nestedStyles(updStyles, inner)
          val suffix    = if (state.invert.isEmpty) EscSeq.resetInvert.bytes else Array.empty[Byte]
          acc ++ prefix ++ nested ++ suffix

        case RichStrikethroughStyle(inner) =>
          val prefix    = if (state.strikethrough.isEmpty) EscSeq.strikethrough.bytes else Array.empty[Byte]
          val updStyles = state.copy(strikethrough = state.strikethrough.appended(()))
          val nested    = nestedStyles(updStyles, inner)
          val suffix    = if (state.strikethrough.isEmpty) EscSeq.resetStrikethrough.bytes else Array.empty[Byte]
          acc ++ prefix ++ nested ++ suffix
      }

    iterate(Array.empty[Byte], State(), root)
  }

}
