package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.eventloops.{ EitherEventLoop, FutureEventLoop, IdEventLoop, TryEventLoop }
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.func.{ Identity, MonadError, Monoid }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

/**
 * Event-Loop Interface
 */
trait EventLoop[F[_]]:
  def run(handler: EventLoop.KeySeqHandler[F]): F[Unit]

object EventLoop:
  import KeySeq.*
  import MonadError.*

  type KeySeqHandler[F[_]] = KeySeq => F[EventLoop.Action]

  def defaultExitKeySeqAction: KeySeq => Action = (ks: KeySeq) =>
    if ks.isEsc then EventLoop.Action.Exit
    else EventLoop.Action.Continue

  given keySeqHandlerMonoid[F[_]: MonadError]: Monoid[KeySeqHandler[F]] with
    def empty: KeySeqHandler[F] =
      (ks: KeySeq) => summon[MonadError[F]].succeed(Action.Continue)

    extension (x: KeySeqHandler[F])
      infix def combine(y: KeySeqHandler[F]): KeySeqHandler[F] =
        (ks: KeySeq) => x(ks).flatMap(a1 => y(ks).map(a2 => a1 | a2))

  sealed trait Action
  object Action:
    case object Continue extends Action
    case object Exit     extends Action

    val empty: Action =
      Continue

    extension (action: Action)
      def isContinue: Boolean =
        action == Continue

      def isExit: Boolean =
        action == Exit

    given actionMonoid: Monoid[Action] with
      def empty: Action =
        Continue

      extension (x: Action)
        infix def combine(y: Action): Action =
          (x, y) match
            case (Action.Exit, _) => Action.Exit
            case (_, Action.Exit) => Action.Exit
            case _                => Action.Continue

  /**
   * Constructors
   */
  def syncEither(term: Term[Either[Throwable, *]]): EventLoop[Either[Throwable, *]] =
    EitherEventLoop.make(term)

  def syncId(term: Term[Identity]): EventLoop[Identity] =
    IdEventLoop.make(term)

  def syncTry(term: Term[Try]): EventLoop[Try] =
    TryEventLoop.make(term)

  def asyncFuture(term: Term[Future])(using ec: ExecutionContext): EventLoop[Future] =
    FutureEventLoop.make(term)
