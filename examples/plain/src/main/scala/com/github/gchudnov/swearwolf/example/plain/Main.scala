package com.github.gchudnov.swearwolf.example.plain

import com.github.gchudnov.swearwolf.*
import com.github.gchudnov.swearwolf.util.*
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.woods.{ AlignStyle, Box, BoxStyle, Graph, GraphStyle, Grid, GridStyle, Label, RichText, Table, TableStyle }
import com.github.gchudnov.swearwolf.util.TextStyleSyntax.styleOps

import java.io.{ FileOutputStream, PrintStream }
import scala.annotation.nowarn
import scala.util.Using
import scala.util.control.Exception.nonFatalCatch

object Main extends App:

  private val logFilePath = "~/swearwolf-plain-example-errors.log"
  private val posKeqSeq   = Point(32, 0)

  nonFatalCatch
    .either({
      Using.resource(Screen.acquireOrThrow()) { (sc: Screen) =>
        val handler = eventHandler(sc)(posKeqSeq) _
        for
          _ <- render(sc)
          _ <- sc.eventLoop(EventLoop.withDefaultHandler(handler))
        yield ()
      }
    })
    .flatten
    .fold(writeFileLog, _ => ())

  private def eventHandler(screen: Screen)(pos: Point)(ks: List[KeySeq]): Either[Throwable, EventLoop.Action] =
    // handle screen resize
    val errOrResize = sequence(ks.filter(_.isInstanceOf[SizeKeySeq]).map { _ =>
      for
        _ <- screen.clear()
        _ <- render(screen)
      yield ()
    })
      .map(_ => ())

    // display key codes
    val errOrDisplay = sequence(ks.zipWithIndex.map { case (keqSeq, i) =>
      screen.put(pos.offset(0, i), keqSeq.toString)
    })
      .map(_ => ())

    for
      _ <- errOrResize
      _ <- errOrDisplay
      _ <- screen.flush()
    yield EventLoop.Action.Continue

  private def render(sc: Screen): Either[Throwable, Unit] =
    import TextStyle.*

    val data = List(10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0, 10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0)

    val b  = Box(Size(21, 3), BoxStyle.SingleBorder)
    val g1 = Graph(Size(16, 1), data, GraphStyle.Dot)
    val g2 = Graph(Size(16, 2), data, GraphStyle.Step)
    val g3 = Graph(Size(16, 2), data, GraphStyle.Quad)
    val gd = Grid(Size(7, 7), Size(3, 3), GridStyle.Frame2)
    val t  = Table(Seq(Seq("111", "222"), Seq("a", "b"), Seq("c", "d")), TableStyle.Frame)
    val l  = Label(Size(16, 4), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)

    for
      rich <- RichText.make("<b>BOLD</b><color fg='#AA0000' bg='#00FF00'>NOR</color>MAL<i>italic</i><k>BLINK</k>")
      _    <- sc.put(Point(0, 0), "HELLO", Bold | Foreground(NamedColor.Blue))
      _    <- sc.put(Point(8, 0), "WORLD!", Foreground(NamedColor.Blue) | Background(NamedColor.Yellow))
      // _    <- sc.put(Point(0, 2), rich)
      // _    <- sc.put(Point(0, 4), b, Foreground(NamedColor.Blue))
      // _    <- sc.put(Point(32, 2), g1, Foreground(NamedColor.Green))
      // _    <- sc.put(Point(32, 4), g2, Foreground(NamedColor.LimeGreen))
      // _    <- sc.put(Point(32, 7), g3, Foreground(NamedColor.Azure))
      // _    <- sc.put(Point(22, 0), gd, Foreground(NamedColor.Yellow))
      // _    <- sc.put(Point(0, 7), t, Foreground(NamedColor.White))
      // _    <- sc.put(Point(0, 13), l, Foreground(NamedColor.Red))
      _    <- sc.flush()
    yield ()

  private def sequence[A, B](es: Seq[Either[A, B]]): Either[A, Seq[B]] =
    es.partitionMap(identity) match
      case (Nil, rights) => Right[A, Seq[B]](rights)
      case (lefts, _)    => Left[A, Seq[B]](lefts.head)

  @nowarn
  private def writeStdoutLog(t: Throwable): Unit =
    writeErrorLog(System.out)(t)

  private def writeFileLog(t: Throwable): Unit =
    val output = new PrintStream(new FileOutputStream(logFilePath, true))
    writeErrorLog(output)(t)

  private def writeErrorLog(output: PrintStream)(t: Throwable): Unit =
    output.println(t.getMessage)
    t.printStackTrace(output)
    output.flush()
