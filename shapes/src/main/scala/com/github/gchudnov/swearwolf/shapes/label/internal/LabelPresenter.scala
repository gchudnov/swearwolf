package com.github.gchudnov.swearwolf.shapes.label.internal

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.strings.Strings.*
import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.shapes.styles.AlignStyle
import com.github.gchudnov.swearwolf.shapes.styles.Layout

private[label] object LabelPresenter:

  def present(label: Label): Seq[String] =
    val lines = compile(label)
    lines

  private def compile(label: Label): Seq[String] =
    val lines          = label.value.wrap(label.size.width)
    val visibleLines   = lines.take(label.size.height)
    val effectiveLines = if lines.size > visibleLines.size then visibleLines.dropRight(1) :+ visibleLines.last.forceEllipsisRight() else lines
    val alignedLines   = effectiveLines.map(line => Layout.align(line, label.size.width, label.align))

    alignedLines
