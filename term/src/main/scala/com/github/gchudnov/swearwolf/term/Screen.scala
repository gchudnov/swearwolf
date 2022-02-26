package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.internal.screens.ArrayScreen
import com.github.gchudnov.swearwolf.term.internal.screens.TermScreen
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait Screen:
  def size: Size

  def put(pt: Point, value: String): Either[Throwable, Unit]
  def put(pt: Point, value: String, style: TextStyle): Either[Throwable, Unit]
  def put(pt: Point, value: Span): Either[Throwable, Unit]
  def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit]

  def cursorHide(): Either[Throwable, Unit]
  def cursorShow(): Either[Throwable, Unit]

  def mouseTrack(): Either[Throwable, Unit]
  def mouseUntrack(): Either[Throwable, Unit]

  def bufferNormal(): Either[Throwable, Unit]
  def bufferAlt(): Either[Throwable, Unit]

  def clear(): Either[Throwable, Unit]

  def flush(): Either[Throwable, Unit]

  // TODO: remove loop here
  def eventLoop(handler: KeySeqHandler): Either[Throwable, Unit]
  def eventLoop(): Either[Throwable, Unit] =
    eventLoop(EventLoop.defaultHandler)

  def eventPoll(): Either[Throwable, List[KeySeq]]

  def close(): Unit

object Screen:

  def term(): Either[Throwable, Screen] =
    val term = Term.make()
    TermScreen.make(term)

  def array(size: Size, cellChar: Char = ArrayScreen.DefaultCellChar, borderChar: Option[Char] = Some(ArrayScreen.DefaultBorderChar)): Either[Throwable, Screen] =
    Right(ArrayScreen.make(size, cellChar, borderChar))

  def compile(span: Span): Bytes =
    TermScreen.compile(span)
