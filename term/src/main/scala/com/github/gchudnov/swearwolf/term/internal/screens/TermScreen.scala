package com.github.gchudnov.swearwolf.term.internal.screens

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.EventLoop.Action
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.internal.spans.SpanCompiler
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.strings.Strings.*
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle.*
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import com.github.gchudnov.swearwolf.util.exec.Exec
import sun.misc.Signal

import scala.annotation.tailrec
import scala.util.control.Exception.nonFatalCatch
import com.github.gchudnov.swearwolf.util.bytes.Bytes

/**
 * Default Screen implementation.
 *
 * NOTE: when calling methods of the class, do not forget to call flush().
 */
private[term] final class TermScreen(term: Term) extends Screen:
  import TermScreen.*
  import KeySeqSyntax.*

  @volatile
  private var szScreen: Size = Size(0, 0)

  override def size: Size = szScreen

  override def put(pt: Point, value: String): Either[Throwable, Unit] =
    put(pt, value.getBytes)

  override def put(pt: Point, value: String, style: TextStyle): Either[Throwable, Unit] =
    val styleBytes = toEscSeq(style).map(_.bytes).reduce(_ ++ _)
    val bytes      = styleBytes ++ value.getBytes ++ EscSeq.reset.bytes
    put(pt, bytes)

  def put(pt: Point, value: Span): Either[Throwable, Unit] =
    val bytes = TermScreen.compile(value)
    put(pt, bytes.toArray)

  override def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit] =
    if pt.y >= szScreen.height then Right(())
    else
      val bytes = EscSeq.cursorPosition(pt).bytes ++ value
      term.write(bytes)

  override def cursorHide(): Either[Throwable, Unit] =
    TermScreen.cursorHide(term)

  override def cursorShow(): Either[Throwable, Unit] =
    TermScreen.cursorShow(term)

  override def mouseTrack(): Either[Throwable, Unit] =
    TermScreen.mouseTrack(term)

  override def mouseUntrack(): Either[Throwable, Unit] =
    TermScreen.mouseUntrack(term)

  override def bufferNormal(): Either[Throwable, Unit] =
    TermScreen.bufferNormal(term)

  override def bufferAlt(): Either[Throwable, Unit] =
    TermScreen.bufferAlt(term)

  override def clear(): Either[Throwable, Unit] =
    term.write(EscSeq.erase)

  override def flush(): Either[Throwable, Unit] =
    term.flush()

  override def init(): Either[Throwable, Unit] =
    for
      _ <- TermScreen.headless(true) //
      _ <- sttyRaw()                 //
      _ <- bufferAlt()               //
      _ <- clear()                   //
      _ <- cursorHide()              //
      _ <- mouseTrack()              //
      _ <- fetchSize()
      _ <- flush()
      _  = handleWinch()
    yield ()

  override def shutdown(): Either[Throwable, Unit] =
    for
      _ <- clear()        //
      _ <- cursorShow()   //
      _ <- mouseUntrack() //
      _ <- bufferNormal() //
      _ <- flush()
      _ <- sttySane()     //
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

  private def trackScreenSize(k: KeySeq): Unit =
    k.size.foreach { sz =>
      szScreen = sz
    }

// TODO: refactor TermScreen, see how to make it more resilient and immutable

private[term] object TermScreen:

  type TermEffect = (Term) => Either[Throwable, Unit]

  private val initEffects: List[(TermEffect, TermEffect)] = List(
    (t => headless(true), t => headless(false)),
    (t => sttyRaw(), t => sttySane()),
    (bufferAlt, bufferNormal),
    (cursorHide, cursorShow),
    (mouseTrack, mouseUntrack)
  )

  def acquire(term: Term): TermScreen =
    ???
    // new TermScreen(term)

  // def release

  private def toEscSeq(style: TextStyle): Seq[EscSeq] =
    style match
      case a: TextStyleSeq =>
        a.styles.flatMap(toEscSeq)
      case TextStyle.Empty =>
        Seq(EscSeq.empty)
      case TextStyle.Foreground(color) =>
        Seq(EscSeq.foreground(color))
      case TextStyle.Background(color) =>
        Seq(EscSeq.background(color))
      case TextStyle.Bold =>
        Seq(EscSeq.bold)
      case TextStyle.Italic =>
        Seq(EscSeq.italic)
      case TextStyle.Underline =>
        Seq(EscSeq.underline)
      case TextStyle.Blink =>
        Seq(EscSeq.blink)
      case TextStyle.Invert =>
        Seq(EscSeq.invert)
      case TextStyle.Strikethrough =>
        Seq(EscSeq.strikethrough)
      case TextStyle.Transparent =>
        Seq.empty[EscSeq]
      case TextStyle.NoColor =>
        Seq.empty[EscSeq]

  /**
   * Compile span to bytes
   */
  def compile(span: Span): Bytes =
    SpanCompiler.compile(span)

  /**
   * Set terminal to raw mode.
   */
  private def sttyRaw(): Either[Throwable, Unit] =
    Exec.exec(Array("sh", "-c", "stty raw -echo < /dev/tty"))

  /**
   * Reset all terminal settings to "sane" values.
   */
  private def sttySane(): Either[Throwable, Unit] =
    Exec.exec(Array("sh", "-c", "stty sane < /dev/tty"))

  /**
   * Set Alt buffer.
   */
  private def bufferAlt(term: Term): Either[Throwable, Unit] =
    term.write(EscSeq.altBuffer)

  /**
   * Set Normal buffer.
   */
  private def bufferNormal(term: Term): Either[Throwable, Unit] =
    term.write(EscSeq.normalBuffer)

  /**
   * Hide cursor.
   */
  private def cursorHide(term: Term): Either[Throwable, Unit] =
    term.write(EscSeq.cursorHide)

  /**
   * Show cursor.
   */
  private def cursorShow(term: Term): Either[Throwable, Unit] =
    term.write(EscSeq.cursorShow)

  /**
   * Mouse tracking.
   */
  private def mouseTrack(term: Term): Either[Throwable, Unit] =
    term.write(EscSeq.mouseTracking)

  /**
   * Mouse stop tracking.
   */
  private def mouseUntrack(term: Term): Either[Throwable, Unit] =
    term.write(EscSeq.resetMouseTracking)

  /**
   * Set headless setting.
   */
  private def headless(flag: Boolean): Either[Throwable, Unit] =
    nonFatalCatch.either(System.setProperty("java.awt.headless", flag.toString))
