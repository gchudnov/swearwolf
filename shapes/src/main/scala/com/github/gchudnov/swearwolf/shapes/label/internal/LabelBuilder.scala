package com.github.gchudnov.swearwolf.shapes.label.internal

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.strings.Strings.*
import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.layout.Layout

private[label] object LabelBuilder:

  def build(label: Label): Seq[Span] =
    val lines = prepare(label)
    lines.map(TextSpan(_))

  private def prepare(label: Label): Seq[String] =
    val lines          = label.value.wrap(label.size.width)
    val visibleLines   = lines.take(label.size.height)
    val effectiveLines = if lines.size > visibleLines.size then visibleLines.dropRight(1) :+ visibleLines.last.forceEllipsisRight() else lines
    val alignedLines   = effectiveLines.map(line => Layout.align(line, label.size.width, label.align))

    alignedLines
