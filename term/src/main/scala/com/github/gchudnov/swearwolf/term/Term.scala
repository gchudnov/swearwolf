package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.terms.*
import com.github.gchudnov.swearwolf.util.func.{ Identity, MonadError, Monoid }

import java.io.{ InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter
import scala.concurrent.Future
import scala.util.Try

trait Term[F[_]]:
  def read(): F[Option[Array[Byte]]]
  def write(bytes: Array[Byte]): F[Unit]
  def flush(): F[Unit]
  def close(): F[Unit]

object Term:

  type TermAction[F[_]] = (Term[F]) => F[Unit]

  object TermAction:
    def apply[F[_]](fn: (Term[F]) => F[Unit]): TermAction[F] =
      fn

    given termActionMonoid[F[_]: MonadError]: Monoid[TermAction[F]] with
      def empty: TermAction[F] =
        _ => summon[MonadError[F]].unit

      extension (x: TermAction[F])
        infix def combine(y: TermAction[F]): TermAction[F] =
          import MonadError.*
          t => x(t).flatMap(_ => y(t))

  extension [F[_]](term: Term[F])
    /**
     * Set Alt buffer.
     */
    def bufferAlt(): F[Unit] =
      term.write(EscSeq.altBuffer.bytes)

    /**
     * Set Normal buffer.
     */
    def bufferNormal(): F[Unit] =
      term.write(EscSeq.normalBuffer.bytes)

    /**
     * Hide cursor.
     */
    def cursorHide(): F[Unit] =
      term.write(EscSeq.cursorHide.bytes)

    /**
     * Show cursor.
     */
    def cursorShow(): F[Unit] =
      term.write(EscSeq.cursorShow.bytes)

    /**
     * Mouse tracking.
     */
    def mouseTrack(): F[Unit] =
      term.write(EscSeq.mouseTracking.bytes)

    /**
     * Mouse stop tracking.
     */
    def mouseUntrack(): F[Unit] =
      term.write(EscSeq.resetMouseTracking.bytes)

    /**
     * Get the size of the terminal
     */
    def fetchSize(): F[Unit] =
      term.write(EscSeq.textAreaSize.bytes)

    /**
     * Clear the Terminal
     */
    def clear(): F[Unit] =
      term.write(EscSeq.erase.bytes)

  /**
   * Constructors
   */
  def syncEither(in: InputStream = System.in, out: OutputStream = System.out): Term[Either[Throwable, *]] =
    EitherSyncTerm.make(in, out)

  def syncId(in: InputStream = System.in, out: OutputStream = System.out): Term[Identity] =
    IdSyncTerm.make(in, out)

  def syncTry(in: InputStream = System.in, out: OutputStream = System.out): Term[Try] =
    TrySyncTerm.make(in, out)

  def asyncFuture(in: InputStream = System.in, out: OutputStream = System.out): Term[Future[*]] =
    FutureSyncTerm.make(in, out)

  /**
   * Logging Constructors
   */
  def syncFileLogEither(
    path: Path,
    term: Term[Either[Throwable, *]] = syncEither(System.in, System.out),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter
  ): Term[Either[Throwable, *]] =
    EitherSyncTerm.fileLog(path, term, isTruncate, fmt)

  def syncFileLogId(
    path: Path,
    term: Term[Identity] = syncId(System.in, System.out),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter
  ): Term[Identity] =
    IdSyncTerm.fileLog(path, term, isTruncate, fmt)

  def syncFileLogTry(
    path: Path,
    term: Term[Try] = syncTry(System.in, System.out),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter
  ): Term[Try] =
    TrySyncTerm.fileLog(path, term, isTruncate, fmt)

  def asyncFileLogFuture(
    path: Path,
    term: Term[Future] = asyncFuture(System.in, System.out),
    isTruncate: Boolean = true,
    fmt: DateTimeFormatter = LogTerm.defaultDateTimeFormatter
  ): Term[Future[*]] =
    FutureSyncTerm.fileLog(path, term, isTruncate, fmt)
