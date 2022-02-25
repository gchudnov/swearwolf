package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.{ Term, TermScreen, EventLoop }
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.styles.TextStyle

import scala.util.Using.Releasable

trait Screen:
  def size: Size

  def put(pt: Point, value: String): Either[Throwable, Unit]
  def put(pt: Point, value: String, style: TextStyle): Either[Throwable, Unit]
  def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit]

  def cursorHide(): Either[Throwable, Unit]
  def cursorShow(): Either[Throwable, Unit]

  def mouseTrack(): Either[Throwable, Unit]
  def mouseUntrack(): Either[Throwable, Unit]

  def bufferNormal(): Either[Throwable, Unit]
  def bufferAlt(): Either[Throwable, Unit]

  def clear(): Either[Throwable, Unit]

  def flush(): Either[Throwable, Unit]

  def init(): Either[Throwable, Unit]
  def shutdown(): Either[Throwable, Unit]

  def eventLoop(handler: KeySeqHandler): Either[Throwable, Unit]
  def eventLoop(): Either[Throwable, Unit] =
    eventLoop(EventLoop.defaultHandler)

  def eventPoll(): Either[Throwable, List[KeySeq]]

  def close(): Unit = shutdown().toTry.get

object Screen:
  implicit val releasableScreen: Releasable[Screen] = screen => screen.close()

  def acquire(): Either[Throwable, Screen] =
    val term   = Term.default()
    val screen = new TermScreen(term)

    screen.init().map(_ => screen)

  def acquireOrThrow(): Screen =
    acquire().toTry.get
