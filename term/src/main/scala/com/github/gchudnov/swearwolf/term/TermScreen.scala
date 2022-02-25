package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.{ KeySeq, Screen }
import com.github.gchudnov.swearwolf.term.EventLoop.{ Action, KeySeqHandler }
import com.github.gchudnov.swearwolf.util.strings.Strings.*
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import com.github.gchudnov.swearwolf.util.styles.TextStyle.*
import com.github.gchudnov.swearwolf.util.geometry.{Size, Point}
import com.github.gchudnov.swearwolf.term.KeySeqSyntax
import sun.misc.Signal

import scala.annotation.tailrec
import scala.util.control.Exception.nonFatalCatch

/**
 * Default Screen implementation.
 *
 * NOTE: when calling methods of the class, do not forget to call flush().
 */
private[term] final class TermScreen(term: Term) extends Screen:
  import KeySeqSyntax.*

  @volatile
  private var szScreen: Size = Size(0, 0)

  override def size: Size = szScreen

  override def put(pt: Point, value: String): Either[Throwable, Unit] =
    put(pt, valueBytes(value))

  override def put(pt: Point, value: String, style: TextStyle): Either[Throwable, Unit] =
    val styleBytes = styleToEscSeq(style).map(_.bytes).reduce(_ ++ _)
    val bytes      = styleBytes ++ valueBytes(value) ++ EscSeq.reset.bytes
    put(pt, bytes)

  override def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit] =
    if pt.y >= szScreen.height then Right(())
    else {
      val bytes = EscSeq.cursorPosition(pt).bytes ++ value
      term.write(bytes)
    }

  override def cursorHide(): Either[Throwable, Unit] =
    term.write(EscSeq.cursorHide)

  override def cursorShow(): Either[Throwable, Unit] =
    term.write(EscSeq.cursorShow)

  override def mouseTrack(): Either[Throwable, Unit] =
    term.write(EscSeq.mouseTracking)

  override def mouseUntrack(): Either[Throwable, Unit] =
    term.write(EscSeq.resetMouseTracking)

  override def bufferNormal(): Either[Throwable, Unit] =
    term.write(EscSeq.normalBuffer)

  override def bufferAlt(): Either[Throwable, Unit] =
    term.write(EscSeq.altBuffer)

  override def clear(): Either[Throwable, Unit] =
    term.write(EscSeq.erase)

  override def flush(): Either[Throwable, Unit] =
    term.flush()

  override def init(): Either[Throwable, Unit] =
    for
      _ <- headless()
      _ <- rawTerm()
      _ <- bufferAlt()
      _ <- clear()
      _ <- cursorHide()
      _ <- mouseTrack()
      _ <- fetchSize()
      _ <- flush()
      _  = handleWinch()
    yield ()

  override def shutdown(): Either[Throwable, Unit] =
    for
      _ <- clear()
      _ <- cursorShow()
      _ <- mouseUntrack()
      _ <- bufferNormal()
      _ <- flush()
      _ <- resetTerm()
    yield ()

  @tailrec
  override def eventLoop(handler: KeySeqHandler): Either[Throwable, Unit] =
    val errOrAction = for
      ks     <- eventPoll()
      action <- handler(ks)
    yield action

    errOrAction match
      case Left(err)                                  => Left(err)
      case Right(action) if action == Action.Continue => eventLoop(handler)
      case _                                          => Right(())

  override def eventPoll(): Either[Throwable, List[KeySeq]] =
    for
      ks <- term.blockingPoll()
      _  <- internalStateHandler(ks)
    yield ks

  private def internalStateHandler(ks: List[KeySeq]): Either[Throwable, Unit] =
    for _ <- Right[Throwable, Unit](ks.foreach(trackScreenSize))
    yield ()

  private def fetchSize(): Either[Throwable, Unit] =
    term.write(EscSeq.textAreaSize)

  private def rawTerm(): Either[Throwable, Unit] =
    exec(Array("sh", "-c", "stty raw -echo < /dev/tty"))

  /**
   * Reset all terminal settings to "sane" values.
   */
  private def resetTerm(): Either[Throwable, Unit] =
    exec(Array("sh", "-c", "stty sane < /dev/tty"))

  /**
   * Execute the given array of arguments.
   */
  private def exec(as: Array[String]): Either[Throwable, Unit] =
    nonFatalCatch.either(Runtime.getRuntime.exec(as))

  /**
   * Set headless setting.
   */
  private def headless(): Either[Throwable, Unit] =
    nonFatalCatch.either(System.setProperty("java.awt.headless", "true"))

  /**
   * Handle window resize.
   */
  private def handleWinch(): Unit =
    Signal.handle(
      new Signal("WINCH"),
      (_: Signal) =>
        for
          _ <- fetchSize()
          _ <- flush()
        yield ()
    )

  private def styleToEscSeq(style: TextStyle): Seq[EscSeq] =
    @tailrec
    def iterate(acc: Vector[EscSeq], s: TextStyle, rest: List[TextStyle]): Vector[EscSeq] =
      (s, rest) match
        case (a: TextStyleSeq, rs) =>
          iterate(acc, a.styles.head, rs ++ a.styles.tail)
        case (x, r :: rs) =>
          iterate(acc.appended(textStyleToEscSeq(x)), r, rs)
        case (x, Nil) =>
          acc.appended(textStyleToEscSeq(x))

    iterate(Vector.empty[EscSeq], style, List.empty[TextStyle])

  private def valueBytes(value: String): Array[Byte] =
    value.sanitize().getBytes

  private def textStyleToEscSeq(s: TextStyle): EscSeq =
    s match
      case Empty =>
        EscSeq.empty
      case Foreground(color) =>
        EscSeq.foreground(color)
      case Background(color) =>
        EscSeq.background(color)
      case Bold =>
        EscSeq.bold
      case Italic =>
        EscSeq.italic
      case Underline =>
        EscSeq.underline
      case Blink =>
        EscSeq.blink
      case Invert =>
        EscSeq.invert
      case Strikethrough =>
        EscSeq.strikethrough
      case _ =>
        EscSeq.empty

  private def trackScreenSize(k: KeySeq): Unit =
    k.size.foreach { sz =>
      szScreen = sz
    }
