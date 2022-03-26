package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.writers.{ EitherWriter, FutureWriter, IdWriter, TryWriter }
import com.github.gchudnov.swearwolf.util.func.Identity
import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

/**
 * Interface to write to a terminal.
 *
 * It is more high-level than writing to the terminal directly that accepts only bytes.
 */
trait Writer[F[_]]:
  def put(value: String): F[Unit]
  def put(value: String, style: TextStyle): F[Unit]
  def put(value: Span): F[Unit]
  def put(value: Array[Byte]): F[Unit]

  def flush(): F[Unit]

object Writer:

  def syncEither(term: Term[Either[Throwable, *]]): Writer[Either[Throwable, *]] =
    EitherWriter.make(term)

  def syncId(term: Term[Identity]): Writer[Identity] =
    IdWriter.make(term)

  def syncTry(term: Term[Try]): Writer[Try] =
    TryWriter.make(term)

  def asyncFuture(term: Term[Future])(using ec: ExecutionContext): Writer[Future] =
    FutureWriter.make(term)
