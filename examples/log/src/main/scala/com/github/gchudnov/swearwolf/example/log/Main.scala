package com.github.gchudnov.swearwolf.example.log

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.rich.instances.EitherRichText.*
import com.github.gchudnov.swearwolf.shapes.box.{ Box, BoxStyle }
import com.github.gchudnov.swearwolf.shapes.chart.{ Chart, ChartStyle }
import com.github.gchudnov.swearwolf.shapes.grid.{ Grid, GridStyle }
import com.github.gchudnov.swearwolf.shapes.instances.EitherShapes.*
import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.shapes.table.{ Table, TableStyle }
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.term.EventLoop.{ Action, KeySeqHandler }
import com.github.gchudnov.swearwolf.term.eventloops.EitherEventLoop
import com.github.gchudnov.swearwolf.term.keys.*
import com.github.gchudnov.swearwolf.term.screens.EitherScreen
import com.github.gchudnov.swearwolf.term.terms.EitherSyncTerm
import com.github.gchudnov.swearwolf.util.clock.InstantClock
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.geometry.*
import com.github.gchudnov.swearwolf.util.logging.FileLogging
import com.github.gchudnov.swearwolf.util.styles.{ AlignStyle, TextStyle }

import java.nio.file.Paths
import scala.util.control.Exception.nonFatalCatch

/**
 * Interactive Example, that uses StdIn to get input and StdOut to display output and logs the process.
 *
 * Uses `Either[Throwable, *]` to represent the result of a computation.
 */
object Main extends App:

  var screenSize: Option[Size] = None

  val logTerm = EitherSyncTerm.fileLog(Paths.get("example-log.log"))

  val resF = for
    screen       <- EitherScreen.make(logTerm)
    eventLoop     = new EitherEventLoop(logTerm)
    keySeqHandler = makeKeySeqHandler(screen)
    _            <- eventLoop.run(keySeqHandler)
    _            <- nonFatalCatch.either(screen.close())
  yield ()

  private def makeKeySeqHandler(screen: Screen[Either[Throwable, *]]): KeySeqHandler[Either[Throwable, *]] =
    (ks: KeySeq) =>
      if (ks.isEsc) then Right(EventLoop.Action.Exit)
      else
        ks match
          case SizeKeySeq(sz) =>
            screenSize = Some(sz)
            for _ <- onKeySeq(screen, ks)
            yield Action.Continue
          case _ =>
            for _ <- onKeySeq(screen, ks)
            yield Action.Continue

  private def onKeySeq(screen: Screen[Either[Throwable, *]], ks: KeySeq): Either[Throwable, Unit] =
    import TextStyle.*

    val screenWidth  = screenSize.map(_.width).getOrElse(80)
    val screenHeight = screenSize.map(_.height).getOrElse(24)

    val data = List(10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0, 10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0)

    val b  = Box(Size(21, 3), BoxStyle.SingleBorder)
    val g1 = Chart(Size(16, 1), data, ChartStyle.Dot)
    val g2 = Chart(Size(16, 2), data, ChartStyle.Step)
    val g3 = Chart(Size(16, 2), data, ChartStyle.Quad)
    val gd = Grid(Size(7, 7), Size(3, 3), GridStyle.Frame2)
    val t  = Table(Seq(Seq("111", "222"), Seq("a", "b"), Seq("c", "d")), TableStyle.Frame)
    val l  = Label(Size(16, 4), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)

    val posLabelX = 32
    val ksLabel   = Label(Size(screenWidth - posLabelX, 1), ks.toString, AlignStyle.Left)

    val rich = RichText("<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>")

    for
      _ <- screen.put(Point(0, 0), "HELLO", Bold | Foreground(Color.Blue))
      _ <- screen.put(Point(8, 0), "WORLD!", Foreground(Color.Blue) | Background(Color.Yellow))
      _ <- screen.putRich(Point(0, 2), rich)
      _ <- screen.putBox(Point(0, 4), b, Foreground(Color.Blue))
      _ <- screen.putChart(Point(32, 2), g1, Foreground(Color.Green))
      _ <- screen.putChart(Point(32, 4), g2, Foreground(Color.LimeGreen))
      _ <- screen.putChart(Point(32, 7), g3, Foreground(Color.Azure))
      _ <- screen.putGrid(Point(22, 0), gd, Foreground(Color.Yellow))
      _ <- screen.putTable(Point(0, 7), t, Foreground(Color.White))
      _ <- screen.putLabel(Point(0, 13), l, Foreground(Color.Red))
      _ <- screen.putLabel(Point(posLabelX, 0), ksLabel)
      _ <- screen.put(Point(22, 13), s"window size: ${screenWidth}x${screenHeight}  ", Foreground(Color.GhostWhite))
      _ <- screen.flush()
    yield ()
