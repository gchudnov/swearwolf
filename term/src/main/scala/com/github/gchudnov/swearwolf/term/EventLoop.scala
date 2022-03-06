package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.util.func.Monoid
import com.github.gchudnov.swearwolf.util.func.MonadError

/**
 * Event-Loop Interface
 */
trait EventLoop[F[_]]:
  def run(handler: KeySeqHandler[F]): F[Unit]

object EventLoop:
  import KeySeqSyntax.*
  import MonadError.*

  type KeySeqHandler[F[_]] = KeySeq => F[EventLoop.Action]

  def defaultExitKeySeqAction = (ks: KeySeq) => 
    if (ks.isEsc) then 
      EventLoop.Action.Exit 
    else
      EventLoop.Action.Continue

  given keySeqHandlerMonoid[F[_]: MonadError]: Monoid[KeySeqHandler[F]] with
    def empty: KeySeqHandler[F] =
      (ks: KeySeq) => summon[MonadError[F]].unit(Action.Continue)

    extension (x: KeySeqHandler[F])
      infix def combine(y: KeySeqHandler[F]): KeySeqHandler[F] =
        (ks: KeySeq) =>
          x(ks).flatMap(a1 => y(ks).map(a2 => a1 | a2))


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

  // def make(term: Term): EventLoop =
  //   TermEventLoop.make(term)
