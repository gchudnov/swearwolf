package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.{ EscSeq, EventLoop, Term }
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.screens.{ EitherScreen, TryScreen }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle

import scala.util.Try

trait Screen[F[_]] extends Writer[F]:
  def put(pt: Point, value: String): F[Unit]
  def put(pt: Point, value: String, style: TextStyle): F[Unit]
  def put(pt: Point, value: Span): F[Unit]
  def put(pt: Point, value: Array[Byte]): F[Unit]

  def close(): F[Unit]

object Screen:

  def syncEither(term: Term[Either[Throwable, *]]): Either[Throwable, Screen[Either[Throwable, *]]] =
    EitherScreen.make(term)

  def syncId() =
    ???

  def syncTry(term: Term[Try]): Try[Screen[Try]] =
    TryScreen.make(term)

  def asyncFuture() =
    ???

  // TODO: add more methods, similar to Term

//   def make(term: Term): Either[Throwable, Screen] =
//     TermScreen.make(term)

//   def array(size: Size, cellChar: Char = ArrayScreen.DefaultCellChar, borderChar: Option[Char] = Some(ArrayScreen.DefaultBorderChar)): Either[Throwable, Screen] =
//     Right(ArrayScreen.make(size, cellChar, borderChar))

//   def compile(span: Span): Bytes =
//     TermScreen.compile(span)
