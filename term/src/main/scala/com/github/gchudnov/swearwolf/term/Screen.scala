package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle

trait Screen[F[_]] extends Writer[F]:
  def put(pt: Point, value: String): F[Unit]
  def put(pt: Point, value: String, style: TextStyle): F[Unit]
  def put(pt: Point, value: Span): F[Unit]
  def put(pt: Point, value: Array[Byte]): F[Unit]


// object Screen:

//   def make(term: Term): Either[Throwable, Screen] =
//     TermScreen.make(term)

//   def array(size: Size, cellChar: Char = ArrayScreen.DefaultCellChar, borderChar: Option[Char] = Some(ArrayScreen.DefaultBorderChar)): Either[Throwable, Screen] =
//     Right(ArrayScreen.make(size, cellChar, borderChar))

//   def compile(span: Span): Bytes =
//     TermScreen.compile(span)
