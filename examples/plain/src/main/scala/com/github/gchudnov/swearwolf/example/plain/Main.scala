package com.github.gchudnov.swearwolf.example.plain

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

import java.io.FileOutputStream
import java.io.PrintStream
import scala.annotation.nowarn
import scala.util.Using
import scala.util.Using.Releasable
import scala.util.control.Exception.nonFatalCatch

object Main extends App:

  private val logFilePath = "~/swearwolf-plain-example-errors.log"

  val term = Term.make()
  val resF = for
    screen       <- Screen.make(term)
    keySeqHandler = makeKeySeqHandler(screen)
    eventLoop     = EventLoop.make(term, keySeqHandler)
    _            <- eventLoop.run()
    _            <- nonFatalCatch.either(screen.close())
  yield ()

  resF.fold(t => writeToStdout, identity)

  private def makeKeySeqHandler(screen: Screen): KeySeqHandler =
    (ks: KeySeq) =>
      ks match
        case SizeKeySeq(sz) =>
          for
            _ <- screen.onSize(sz)
            _ <- screen.clear()
            _ <- render(screen, "")
          yield Action.Continue
        case _ =>
          for _ <- render(screen, ks.toString)
          yield Action.Continue

  private def render(screen: Screen, text: String): Either[Throwable, Unit] =
    import TextStyle.*

    val sz = screen.size

    val data = List(10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0, 10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0)

    val b  = Box(Size(21, 3), BoxStyle.SingleBorder)
    val g1 = Chart(Size(16, 1), data, ChartStyle.Dot)
    val g2 = Chart(Size(16, 2), data, ChartStyle.Step)
    val g3 = Chart(Size(16, 2), data, ChartStyle.Quad)
    val gd = Grid(Size(7, 7), Size(3, 3), GridStyle.Frame2)
    val t  = Table(Seq(Seq("111", "222"), Seq("a", "b"), Seq("c", "d")), TableStyle.Frame)
    val l  = Label(Size(16, 4), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)

    val rich = RichText("<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>")

    for
      _ <- screen.put(Point(0, 0), "HELLO", Bold | Foreground(Color.Blue))
      _ <- screen.put(Point(8, 0), "WORLD!", Foreground(Color.Blue) | Background(Color.Yellow))
      _ <- screen.put(Point(0, 2), rich)
      _ <- screen.put(Point(0, 4), b, Foreground(Color.Blue))
      _ <- screen.put(Point(32, 2), g1, Foreground(Color.Green))
      _ <- screen.put(Point(32, 4), g2, Foreground(Color.LimeGreen))
      _ <- screen.put(Point(32, 7), g3, Foreground(Color.Azure))
      _ <- screen.put(Point(22, 0), gd, Foreground(Color.Yellow))
      _ <- screen.put(Point(0, 7), t, Foreground(Color.White))
      _ <- screen.put(Point(0, 13), l, Foreground(Color.Red))
      _ <- screen.put(Point(32, 0), text)
      _ <- screen.put(Point(22, 13), s"window size: ${sz.width}x${sz.height}", Foreground(Color.GhostWhite))
      _ <- screen.flush()
    yield ()

  private def sequence[A, B](es: Seq[Either[A, B]]): Either[A, Seq[B]] =
    es.partitionMap(identity) match
      case (Nil, rights) => Right[A, Seq[B]](rights)
      case (lefts, _)    => Left[A, Seq[B]](lefts.head)

  @nowarn
  private def writeToStdout(t: Throwable): Unit =
    writeErrorLog(System.out)(t)

  @nowarn
  private def writeToFile(t: Throwable): Unit =
    val output = new PrintStream(new FileOutputStream(logFilePath, true))
    writeErrorLog(output)(t)

  private def writeErrorLog(output: PrintStream)(t: Throwable): Unit =
    output.println(t.getMessage)
    t.printStackTrace(output)
    output.flush()
