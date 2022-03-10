package com.github.gchudnov.swearwolf.term.internal.screens

import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.term.EscSeq
import com.github.gchudnov.swearwolf.util.styles.TextStyleSeq
import com.github.gchudnov.swearwolf.util.exec.Exec
import sun.misc.Signal
import sun.misc.SignalHandler
import com.github.gchudnov.swearwolf.util.func.MonadError

type TermAction[F[_]] = (Term[F]) => F[Unit]

sealed trait ShellScreen:
  import Term.*

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

  /**
   * Listen to window size change.
   */
  private def setWinchListener[F[_]: MonadError](sigHandler: SignalHandler): F[Unit] =
    summon[MonadError[F]].attempt(
      Signal.handle(
        new Signal("WINCH"),
        sigHandler
      )
    )

  private def noOp[F[_]: MonadError](): F[Unit] =
    summon[MonadError[F]].succeed(())

  /**
   * Pairs of init and rollback functions.
   */
  def initRollbackActions[F[_]: MonadError](sigHandler: SignalHandler): List[(TermAction[F], TermAction[F])] =
    List(
      (t => headless(true), t => headless(false)),
      (t => sttyRaw(), t => sttySane()),
      (t => noOp(), t => t.flush()),
      (t => t.bufferAlt(), t => t.bufferNormal()),
      (t => t.cursorHide(), t => t.cursorShow()),
      (t => t.mouseTrack(), t => t.mouseUntrack()),
      (t => setWinchListener(sigHandler), t => noOp()),
      (t => t.fetchSize(), t => noOp()),
      (t => t.flush(), t => noOp())
    )

  /**
   * Given a collection of TermActions, wrap them in a TermAction that can be executed.
   */
  def makeActionExecutor[F[_]: MonadError](rs: Seq[TermAction[F]]): TermAction[F] = (term: Term[F]) =>
    import com.github.gchudnov.swearwolf.util.func.MonadError.*
    summon[MonadError[F]].traverse(rs)(f => f(term)).map(_ => ())

object ShellScreen extends ShellScreen
