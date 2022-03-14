package com.github.gchudnov.swearwolf.example.either

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
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.Action
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.term.keys.*
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.geometry.*
import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.rich.EitherRichText.*

import scala.util.control.Exception.nonFatalCatch
import com.github.gchudnov.swearwolf.term.internal.terminals.EitherTerm
import com.github.gchudnov.swearwolf.term.internal.screens.EitherScreen
import com.github.gchudnov.swearwolf.term.internal.eventloops.EitherEventLoop

object Main extends App:

  val term = EitherSyncTerm.make()

  val resF = for
    screen       <- EitherScreen.make(term)
    eventLoop     = new EitherEventLoop(term)
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
            for _ <- onKeySeq(screen, ks)
            yield Action.Continue
          case _ =>
            for _ <- onKeySeq(screen, ks)
            yield Action.Continue

  private def onKeySeq(screen: Screen[Either[Throwable, *]], ks: KeySeq): Either[Throwable, Unit] =
    import TextStyle.*

    val sz = Size(256, 256)

    val data = List(10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0, 10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0)

    val b  = Box(Size(21, 3), BoxStyle.SingleBorder)
    val g1 = Chart(Size(16, 1), data, ChartStyle.Dot)
    val g2 = Chart(Size(16, 2), data, ChartStyle.Step)
    val g3 = Chart(Size(16, 2), data, ChartStyle.Quad)
    val gd = Grid(Size(7, 7), Size(3, 3), GridStyle.Frame2)
    val t  = Table(Seq(Seq("111", "222"), Seq("a", "b"), Seq("c", "d")), TableStyle.Frame)
    val l  = Label(Size(16, 4), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)

    val ksLabel = Label(Size(sz.width - 32, 1), ks.toString, AlignStyle.Left)

    val rich = RichText("<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>")

    for
      _ <- screen.put(Point(0, 0), "HELLO", Bold | Foreground(Color.Blue))
      _ <- screen.put(Point(8, 0), "WORLD!", Foreground(Color.Blue) | Background(Color.Yellow))
      _ <- screen.putRich(Point(0, 2), rich)
      // _ <- screen.put(Point(0, 4), b, Foreground(Color.Blue))
      // _ <- screen.put(Point(32, 2), g1, Foreground(Color.Green))
      // _ <- screen.put(Point(32, 4), g2, Foreground(Color.LimeGreen))
      // _ <- screen.put(Point(32, 7), g3, Foreground(Color.Azure))
      // _ <- screen.put(Point(22, 0), gd, Foreground(Color.Yellow))
      // _ <- screen.put(Point(0, 7), t, Foreground(Color.White))
      // _ <- screen.put(Point(0, 13), l, Foreground(Color.Red))
      // _ <- screen.put(Point(32, 0), ksLabel)
      // _ <- screen.put(Point(22, 13), s"window size: ${sz.width}x${sz.height}", Foreground(Color.GhostWhite))
      _ <- screen.flush()
    yield ()
