package com.github.gchudnov.swearwolf.term.internal.screens

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.EventLoop.Action
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.internal.screens.TermScreen.TermEffect
import com.github.gchudnov.swearwolf.term.internal.spans.SpanCompiler
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.exec.Exec
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.strings.Strings.*
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle.*
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import sun.misc.Signal

import scala.annotation.tailrec
import scala.util.control.Exception.nonFatalCatch
import com.github.gchudnov.swearwolf.util.func.Transform

/**
 * Default Screen implementation.
 *
 * NOTE: when calling methods of the class, do not forget to call flush().
 */
private[screens] final class TermScreen(term: Term, rollback: List[TermEffect]) extends BasicScreen:
  import TermScreen.*
  import KeySeqSyntax.*

  @volatile
  private var szScreen: Size = Size(1024, 1024)

  override def size: Size = szScreen

  override def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit] =
    if pt.y >= szScreen.height || pt.y < 0 then Right(())
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
    TermScreen.clear(term)

  override def flush(): Either[Throwable, Unit] =
    TermScreen.flush(term)

  override def close(): Unit =
    val err = Transform.sequence(rollback.map(_.apply(term)))
    err.toTry.get

  private def fetchSize(): Either[Throwable, Unit] =
    term.write(EscSeq.textAreaSize)

  private def onWinch(): Either[Throwable, Unit] =
    for
      _ <- fetchSize()
      _ <- flush()
    yield ()

  private def trackScreenSize(k: KeySeq): Unit =
    k.size.foreach { sz =>
      szScreen = sz
    }

private[term] object TermScreen:

  type TermEffect = (Term) => Either[Throwable, Unit]

  // TODO: winch
  // TODO: store cursor pos?

  /**
   * Effect -> Fallback mapping.
   */
  private val initEffects: List[(TermEffect, TermEffect)] =
    List(
      (t => headless(true), t => headless(false)),
      (t => sttyRaw(), t => sttySane()),
      (noOp, flush),
      (bufferAlt, bufferNormal),
      (cursorHide, cursorShow),
      (mouseTrack, mouseUntrack),
      (flush, noOp)
    )

  /**
   * Makes a new TermScreen.
   */
  def make(term: Term): Either[Throwable, TermScreen] =
    val (errOrOk, rollback) = initialize(term)
    errOrOk match
      case Left(t) =>
        // NOTE: we roll-back and ignore the errors to avoid error in the error
        rollback.foreach(_(term))
        Left(t)
      case Right(_) =>
        val screen = new TermScreen(term, rollback)
        Right(screen)

  /**
   * Compile span to bytes
   */
  def compile(span: Span): Bytes =
    SpanCompiler.compile(span)

  private def initialize(term: Term): (Either[Throwable, Unit], List[TermEffect]) =
    initEffects.foldLeft((Right(()): Either[Throwable, Unit], List.empty[TermEffect])) { case ((err, acc), eff) =>
      err match
        case Left(t) => (Left(t), acc)
        case Right(_) =>
          val (effect, rollback) = eff
          effect(term) match
            case Left(t)  => (Left(t), acc)
            case Right(_) => (Right(()), rollback :: acc)
    }

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
   * Flush terminal.
   */
  private def flush(term: Term): Either[Throwable, Unit] =
    term.flush()

  /**
   * Clear the Terminal
   */
  private def clear(term: Term): Either[Throwable, Unit] =
    term.write(EscSeq.erase)

  /**
   * No-Op
   */
  private def noOp(term: Term): Either[Throwable, Unit] =
    Right(())

  /**
   * Set headless setting.
   */
  private def headless(flag: Boolean): Either[Throwable, Unit] =
    nonFatalCatch.either(System.setProperty("java.awt.headless", flag.toString))

  /**
   * Listen to window size change.
   */
  private def setWinchListener(cb: () => Unit): Unit =
    Signal.handle(
      new Signal("WINCH"),
      (_: Signal) => cb()
    )

  /**
   * Remove window-size-change listener
   */
  private def unsetWinchListener(): Unit =
    ()
