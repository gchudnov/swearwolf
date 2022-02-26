package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax

/**
 * Event-Loop Interface
 */
trait EventLoop:
  def run(): Unit
  def poll(): Option[KeySeq]

object EventLoop:
  import KeySeqSyntax.*

  type KeySeqHandler = List[KeySeq] => Either[Throwable, EventLoop.Action]

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

  // TODO: use monoid here
  def mergeActions(lhs: Action, rhs: Action): Action =
    (lhs, rhs) match
      case (Action.Exit, _) => Action.Exit
      case (_, Action.Exit) => Action.Exit
      case _                => Action.Continue

  // TODO: use monoid here
  def combineHandlers(h1: KeySeqHandler, h2: KeySeqHandler): KeySeqHandler = (ks: List[KeySeq]) =>
    for
      r1 <- h1(ks)
      r2 <- h2(ks)
    yield EventLoop.mergeActions(r1, r2)

  def withDefaultHandler(handler: KeySeqHandler): KeySeqHandler =
    combineHandlers(handler, defaultHandler)

  val defaultHandler: KeySeqHandler = (ks: List[KeySeq]) =>
    val initialState: EventLoop.Action = Action.Continue
    val res = ks.foldLeft(initialState) { (acc, k) =>
      val act =
        if k.isEsc then Action.Exit
        else Action.Continue

      EventLoop.mergeActions(act, acc)
    }
    Right(res)
