package com.github.gchudnov.swearwolf.example.zio.internal

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.shapes.box.Box
import com.github.gchudnov.swearwolf.shapes.box.BoxStyle
import com.github.gchudnov.swearwolf.shapes.chart.Chart
import com.github.gchudnov.swearwolf.shapes.chart.ChartStyle
import com.github.gchudnov.swearwolf.shapes.grid.Grid
import com.github.gchudnov.swearwolf.shapes.grid.GridStyle
import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.shapes.table.Table
import com.github.gchudnov.swearwolf.shapes.table.TableStyle
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.geometry.*
import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import zio.Queue
import zio.*
import zio.stream.ZStream

final class TermRun(screen: Screen, msgQueue: Queue[Either[Unit, KeySeq]]) extends Run:

  override def onKeySeq(ks: KeySeq): UIO[Unit] =
    msgQueue.offer(Right(ks)).unit

  override def onTick(): UIO[Unit] =
    msgQueue.offer(Left(())).unit

  // def keyLoop(): Task[Unit] =
  //   ZStream
  //     .fromQueue(keqSeqQueue)
  //     .mapZIO(ks => render(Some(ks)))
  //     .runDrain

  // def tickLoop(): Task[Unit] =
  //   ZStream
  //     .fromQueue(tickQueue)
  //     .mapZIO(_ => render(None))
  //     .runDrain

  override def close(): UIO[Unit] =
    for
      _ <- msgQueue.shutdown
    yield ()

//   private def render(ks: Option[KeySeq]): Task[Unit] =
//     import TextStyle.*

//     // val keqSeqPos: Point = Point(32, 0)

//     // val data = List(10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0, 10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0)

//     // val b  = Box(Size(21, 3), BoxStyle.SingleBorder)
//     // val g1 = Chart(Size(16, 1), data, ChartStyle.Dot)
//     // val g2 = Chart(Size(16, 2), data, ChartStyle.Step)
//     // val g3 = Chart(Size(16, 2), data, ChartStyle.Quad)
//     // val gd = Grid(Size(7, 7), Size(3, 3), GridStyle.Frame2)
//     // val t  = Table(Seq(Seq("111", "222"), Seq("a", "b"), Seq("c", "d")), TableStyle.Frame)
//     // val l  = Label(Size(16, 4), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)

//     // val rich = RichText("<b>BOLD</b><color fg='#AA0000' bg='#00FF00'>NOR</color>MAL<i>italic</i><k>BLINK</k>")

//     val errOrUnit = for
//       _ <- screen.clear()
//       _ <- screen.put(Point(0, 0), "HELLO", TextStyle.Bold | TextStyle.Foreground(Color.Blue))
//       // _ <- screen.put(Point(8, 0), "WORLD!", Foreground(Color.Blue) | TextStyle.Background(Color.Yellow))
//       // _ <- screen.put(Point(0, 2), rich)
//       // _ <- screen.put(Point(0, 4), b, TextStyle.Foreground(Color.Blue))
//       // _ <- screen.put(Point(32, 2), g1, TextStyle.Foreground(Color.Green))
//       // _ <- screen.put(Point(32, 4), g2, TextStyle.Foreground(Color.LimeGreen))
//       // _ <- screen.put(Point(32, 7), g3, TextStyle.Foreground(Color.Azure))
//       // _ <- screen.put(Point(22, 0), gd, TextStyle.Foreground(Color.Yellow))
//       // _ <- screen.put(Point(0, 7), t, TextStyle.Foreground(Color.White))
//       // _ <- screen.put(Point(0, 13), l, TextStyle.Foreground(Color.Red))
//       // _ <- screen.put(keqSeqPos.offset(0, 0), ks.fold("")(_.toString))
//       _ <- screen.flush()
//     yield ()

//     ZIO.fromEither(errOrUnit)
//       .catchAll(e => renderError(e))

//   private def renderError(e: Throwable): Task[Unit] =
//     val errOrUnit = for
//       _ <- screen.clear()
//       _ <- screen.put(Point(0, 0), "ERROR", TextStyle.Bold | TextStyle.Foreground(Color.Red))
//       _ <- screen.put(Point(0, 2), e.getMessage)
//       _ <- screen.flush()
//     yield ()

//     ZIO.fromEither(errOrUnit)

//   private def throwableToString(t: Throwable): String =
//     t.getStackTrace.map(ste => ste.toString).mkString(TermRun.lineSep)

object TermRun:
  private val lineSep   = sys.props("line.separator")
  private val queueSize = 128

  def layer: ZLayer[Screen, Throwable, Run] =
    (for
      screen      <- ZIO.service[Screen]
      msgQueue <- Queue.bounded[Either[Unit, KeySeq]](queueSize)
      service      = new TermRun(screen, msgQueue)
    yield service).toLayer
