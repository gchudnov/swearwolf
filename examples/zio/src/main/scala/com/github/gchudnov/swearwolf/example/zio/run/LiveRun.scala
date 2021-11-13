package com.github.gchudnov.swearwolf.example.zio.run

import com.github.gchudnov.swearwolf.util.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.util._
import com.github.gchudnov.swearwolf.woods.{ AlignStyle, Box, BoxStyle, Graph, GraphStyle, Grid, GridStyle, Label, RichText, Table, TableStyle }
import com.github.gchudnov.swearwolf.{ KeySeq, Screen }
import zio._

final class LiveRun(screen: Screen) extends Run {

  var lastKeqSeq: List[KeySeq] = List.empty[KeySeq]

  override def eventHandler: ZIO[Any, Throwable, KeySeqHandler] = ZIO.attempt { (ks: List[KeySeq]) =>
    lastKeqSeq = ks
    Right(EventLoop.Action.Continue)
  }

  override def processLoop(): ZIO[Any, Throwable, Unit] =
    for {
      _ <- ZIO.fromEither(screen.clear())
      _ <- ZIO.fromEither(render(screen))
      _ <- ZIO.fromEither(screen.flush())
    } yield ()

  private def render(sc: Screen): Either[Throwable, Unit] = {
    import TextStyle._

    val keqSeqPos: Point = Point(32, 0)

    val data = List(10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0, 10.0, 56.0, 25.0, 112.0, 45.9, 92.1, 8.0, 12.0)

    val b  = Box(Size(21, 3), BoxStyle.SingleBorder)
    val g1 = Graph(Size(16, 1), data, GraphStyle.Dot)
    val g2 = Graph(Size(16, 2), data, GraphStyle.Step)
    val g3 = Graph(Size(16, 2), data, GraphStyle.Quad)
    val gd = Grid(Size(7, 7), Size(3, 3), GridStyle.Frame2)
    val t  = Table(Seq(Seq("111", "222"), Seq("a", "b"), Seq("c", "d")), TableStyle.Frame)
    val l  = Label(Size(16, 4), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)

    for {
      rich <- RichText.make("<b>BOLD</b><color fg='#AA0000' bg='#00FF00'>NOR</color>MAL<i>italic</i><k>BLINK</k>")
      _    <- sc.put(Point(0, 0), "HELLO", Bold | Foreground(NamedColor.Blue))
      _    <- sc.put(Point(8, 0), "WORLD!", Foreground(NamedColor.Blue) | Background(NamedColor.Yellow))
      _    <- sc.put(Point(0, 2), rich)
      _    <- sc.put(Point(0, 4), b, Foreground(NamedColor.Blue))
      _    <- sc.put(Point(32, 2), g1, Foreground(NamedColor.Green))
      _    <- sc.put(Point(32, 4), g2, Foreground(NamedColor.LimeGreen))
      _    <- sc.put(Point(32, 7), g3, Foreground(NamedColor.Azure))
      _    <- sc.put(Point(22, 0), gd, Foreground(NamedColor.Yellow))
      _    <- sc.put(Point(0, 7), t, Foreground(NamedColor.White))
      _    <- sc.put(Point(0, 13), l, Foreground(NamedColor.Red))
      _ <- sequence(lastKeqSeq.zipWithIndex.map { case (keqSeq, i) =>
             screen.put(keqSeqPos.offset(0, i), keqSeq.toString)
           })
      _ <- sc.flush()
    } yield ()
  }

  private def sequence[A, B](es: Seq[Either[A, B]]): Either[A, Seq[B]] =
    es.partitionMap(identity) match {
      case (Nil, rights) => Right[A, Seq[B]](rights)
      case (lefts, _)    => Left[A, Seq[B]](lefts.head)
    }
}

object LiveRun {

  def layer: ZServiceBuilder[Has[Screen], Throwable, Has[Run]] =
    (for {
      screen <- ZIO.service[Screen]
      service = new LiveRun(screen)
    } yield service).toServiceBuilder

}
