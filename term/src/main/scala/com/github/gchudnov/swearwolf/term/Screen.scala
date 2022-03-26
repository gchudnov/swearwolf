package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.screens.{ EitherScreen, FutureScreen, IdScreen, TryScreen }
import com.github.gchudnov.swearwolf.term.{ EscSeq, EventLoop, Term }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.Identity
import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle

import scala.concurrent.{ ExecutionContext, Future }
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

  def syncId(term: Term[Identity]): Identity[Screen[Identity]] =
    IdScreen.make(term)

  def syncTry(term: Term[Try]): Try[Screen[Try]] =
    TryScreen.make(term)

  def asyncFuture(term: Term[Future])(using ec: ExecutionContext): Future[Screen[Future]] =
    FutureScreen.make(term)
