package com.github.gchudnov.swearwolf.term.internal.screens

import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.exec.Exec
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import sun.misc.Signal
import sun.misc.SignalHandler

type TermAction[F[_]] = (Term[F]) => F[Unit]

object ShellScreen:
  import Term.*

  val SIGWINCH: Signal = new Signal("WINCH")

  /**
   * Set terminal to raw mode.
   */
  private def sttyRaw[F[_]: MonadError](): F[Unit] =
    Exec.exec[F](Array("sh", "-c", "stty raw -echo < /dev/tty"))

  /**
   * Reset all terminal settings to "sane" values.
   */
  private def sttySane[F[_]: MonadError](): F[Unit] =
    Exec.exec[F](Array("sh", "-c", "stty sane < /dev/tty"))

  /**
   * Set headless setting.
   */
  private def headless[F[_]: MonadError](flag: Boolean): F[Unit] =
    summon[MonadError[F]].attempt(System.setProperty("java.awt.headless", flag.toString))

  private def noOp[F[_]: MonadError](): F[Unit] =
    summon[MonadError[F]].succeed(())

  /**
   * Pairs of init and rollback functions.
   */
  def initRollbackActions[F[_]: MonadError](): List[(TermAction[F], TermAction[F])] =
    List(
      (t => headless(true), t => headless(false)),
      (t => sttyRaw(), t => sttySane()),
      (t => noOp(), t => t.flush()),
      (t => t.bufferAlt(), t => t.bufferNormal()),
      (t => t.cursorHide(), t => t.cursorShow()),
      (t => t.mouseTrack(), t => t.mouseUntrack()),
      (t => t.fetchSize(), t => noOp()),
      (t => t.flush(), t => noOp())
    )

  /**
   * Given a collection of TermActions, wrap them in a TermAction that can be executed.
   */
  def makeActionExecutor[F[_]: MonadError](xs: Seq[TermAction[F]]): TermAction[F] = (term: Term[F]) =>
    import com.github.gchudnov.swearwolf.util.func.MonadError.*
    summon[MonadError[F]].traverse(xs)(f => f(term)).unit

  /**
   * Initialize Terminal. If there is an error, roll it back
   *
   * Returns an action that need to be executed to clean-up the terminal.
   */
  def initTerm[F[_]](term: Term[F], pairs: List[(TermAction[F], TermAction[F])])(using ME: MonadError[F]): F[TermAction[F]] =
    import com.github.gchudnov.swearwolf.util.func.MonadError.*
    ME.foldLeft(pairs)(List.empty[TermAction[F]]) { case (acc, x) =>
      val (init, rollback) = x
      init(term).map(_ => rollback +: acc).handleErrorWith(t => ME.traverse(acc)(f => f(term)).flatMap(_ => ME.fail(t)))
    }.map(rs => makeActionExecutor(rs))
