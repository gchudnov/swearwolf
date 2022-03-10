package com.github.gchudnov.swearwolf.shapes.table

import com.github.gchudnov.swearwolf.shapes.table.TableStyle
import com.github.gchudnov.swearwolf.shapes.table.internal.TableBuilder
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.func.MonadError

final case class Table(rows: Seq[Seq[Any]], style: TableStyle):
  def isEmpty: Boolean =
    rows.isEmpty

  def height: Int =
    rows.size

object Table:

  extension (t: Table)
    def estimateSize(): Size =
      TableBuilder.estimateSize(t)

  def build[F[_]: MonadError](table: Table): F[Seq[Span]] =
    given ME: MonadError[F] = summon[MonadError[F]]
    ME.succeed(TableBuilder.build(table))

  extension [F[_]: MonadError](screen: Screen[F])
    def put(pt: Point, table: Table, textStyle: TextStyle): F[Unit] =
      import MonadError.*
      given ME: MonadError[F] = summon[MonadError[F]]

      for
        spans <- build(table)
        _     <- ME.sequence(spans.zipWithIndex.map { case (span, y) => screen.put(pt.offset(0, y), StyleSpan(textStyle, Seq(span))) })
      yield ()

    def put(pt: Point, table: Table): F[Unit] =
      put(pt, table, TextStyle.Empty)
