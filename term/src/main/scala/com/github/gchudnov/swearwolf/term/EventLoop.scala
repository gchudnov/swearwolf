package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.internal.eventloop.TermEventLoop
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.util.internal.Monoid

/**
 * Event-Loop Interface
 */
trait EventLoop:
  def run(): Either[Throwable, Unit]
  def poll(): Either[Throwable, Option[KeySeq]]

object EventLoop:
  import KeySeqSyntax.*

  type KeySeqHandler = KeySeq => Either[Throwable, EventLoop.Action]

  given keySeqHandlerMonoid: Monoid[KeySeqHandler] with
    def empty: KeySeqHandler =
      (ks: KeySeq) => Right(Action.Continue)

    extension (x: KeySeqHandler)
      infix def combine(y: KeySeqHandler): KeySeqHandler =
        (ks: KeySeq) =>
          val w1 = x(ks)
          val w2 = y(ks)
          (w1, w2) match
            case (Left(e1), Left(e2))   => Left(e1)
            case (Left(e1), Right(_))   => Left(e1)
            case (Right(_), Left(e2))   => Left(e2)
            case (Right(a1), Right(a2)) => Right(a1 | a2)

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

  def term(term: Term, handler: KeySeqHandler): EventLoop =
    TermEventLoop.make(term, handler)
