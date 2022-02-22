package com.github.gchudnov.swearwolf.example.zio.run

import com.github.gchudnov.swearwolf.util.geometry.*
import com.github.gchudnov.swearwolf.woods.{ AlignStyle, Box, BoxStyle, Graph, GraphStyle, Grid, GridStyle, Label, RichText, Table, TableStyle }
import com.github.gchudnov.swearwolf.woods.text.RichTextSyntax.*
import com.github.gchudnov.swearwolf.{ KeySeq, Screen }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import zio.stream.ZStream
import zio.{ Queue, * }

final class LiveRun(screen: Screen, keqSeqQueue: Queue[KeySeq]) extends Run:

  val keySeqStream: ZStream[Any, Nothing, KeySeq] =
    ZStream.fromQueue(keqSeqQueue)

  override def onKeySeq(ks: KeySeq): UIO[Unit] =
    keqSeqQueue.offer(ks).unit

  def processLoop(): Task[Unit] =
    keySeqStream
      .mapZIO(ks => render(ks))
      .runDrain

  override def shutdown(): UIO[Unit] =
    for _ <- keqSeqQueue.shutdown
    yield ()

  private def render(ks: KeySeq): Task[Unit] =
    import TextStyle.*

    val keqSeqPos: Point = Point(32, 0)

    val data = List(10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0, 10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0)

    val b  = Box(Size(21, 3), BoxStyle.SingleBorder)
    val g1 = Graph(Size(16, 1), data, GraphStyle.Dot)
    val g2 = Graph(Size(16, 2), data, GraphStyle.Step)
    val g3 = Graph(Size(16, 2), data, GraphStyle.Quad)
    val gd = Grid(Size(7, 7), Size(3, 3), GridStyle.Frame2)
    val t  = Table(Seq(Seq("111", "222"), Seq("a", "b"), Seq("c", "d")), TableStyle.Frame)
    val l  = Label(Size(16, 4), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)

    val errOrUnit = for
      _    <- screen.clear()
      rich <- RichText.make("<b>BOLD</b><color fg='#AA0000' bg='#00FF00'>NOR</color>MAL<i>italic</i><k>BLINK</k>")
      // _    <- screen.put(Point(0, 0), "HELLO", TextStyle.Bold | Foreground(Color.Blue))
      // _    <- screen.put(Point(8, 0), "WORLD!", Foreground(Color.Blue) | Background(Color.Yellow))
      _    <- screen.put(Point(0, 2), rich)
      // _    <- screen.put(Point(0, 4), b, Foreground(Color.Blue))
      // _    <- screen.put(Point(32, 2), g1, Foreground(Color.Green))
      // _    <- screen.put(Point(32, 4), g2, Foreground(Color.LimeGreen))
      // _    <- screen.put(Point(32, 7), g3, Foreground(Color.Azure))
      // _    <- screen.put(Point(22, 0), gd, Foreground(Color.Yellow))
      // _    <- screen.put(Point(0, 7), t, Foreground(Color.White))
      // _    <- screen.put(Point(0, 13), l, Foreground(Color.Red))
      _    <- screen.put(keqSeqPos.offset(0, 0), ks.toString)
      _    <- screen.flush()
    yield ()

    ZIO.fromEither(errOrUnit)

object LiveRun:
  private val queueSize = 16

  def layer: ZLayer[Screen, Throwable, Run] =
    (for
      screen      <- ZIO.service[Screen]
      keqSeqQueue <- Queue.bounded[KeySeq](queueSize)
      service      = new LiveRun(screen, keqSeqQueue)
    yield service).toLayer
