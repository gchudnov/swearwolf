package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.terms.EitherSyncTerm
import com.github.gchudnov.swearwolf.util.func.Monoid
import com.github.gchudnov.swearwolf.util.func.MonadError

trait Term[F[_]]:
  def read(): F[Option[Array[Byte]]]
  def write(bytes: Array[Byte]): F[Unit]
  def flush(): F[Unit]
  def close(): F[Unit]

object Term:

  type TermAction[F[_]] = (Term[F]) => F[Unit]

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
  def syncEither(): Term[Either[Throwable, *]] =
    EitherSyncTerm.make()


// TODO: add methods to create terminals
  // def syncEither
  // def syncId
  // def syncTry
  // def asyncFuture
  // TODO: for ZIO, add:
  // def asyncZIO in ZIO-library