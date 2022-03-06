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

// TODO: add writer trait so we can implement it as an object and make a passive screen ? Add an example

trait Screen[F[_]]:
  def size: Size

  def onSize(sz: Size): F[Unit]

  // TODO: extract to writer

  def put(value: String): F[Unit]
  def put(value: String, style: TextStyle): F[Unit]
  def put(value: Span): F[Unit]
  def put(value: Array[Byte]): F[Unit]

  def put(pt: Point, value: String): F[Unit]
  def put(pt: Point, value: String, style: TextStyle): F[Unit]
  def put(pt: Point, value: Span): F[Unit]
  def put(pt: Point, value: Array[Byte]): F[Unit]

  // TODO: ^^^ extract to writer

  def cursorHide(): F[Unit]
  def cursorShow(): F[Unit]

  def mouseTrack(): F[Unit]
  def mouseUntrack(): F[Unit]

  def bufferNormal(): F[Unit]
  def bufferAlt(): F[Unit]

  def clear(): F[Unit]
  def flush(): F[Unit]

  def close(): Unit

// object Screen:

//   def make(term: Term): Either[Throwable, Screen] =
//     TermScreen.make(term)

//   def array(size: Size, cellChar: Char = ArrayScreen.DefaultCellChar, borderChar: Option[Char] = Some(ArrayScreen.DefaultBorderChar)): Either[Throwable, Screen] =
//     Right(ArrayScreen.make(size, cellChar, borderChar))

//   def compile(span: Span): Bytes =
//     TermScreen.compile(span)
