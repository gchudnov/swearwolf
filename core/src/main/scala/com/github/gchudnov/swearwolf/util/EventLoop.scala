package com.github.gchudnov.swearwolf.util

import com.github.gchudnov.swearwolf.KeySeq

object EventLoop {

  type KeySeqHandler = List[KeySeq] => Either[Throwable, EventLoop.Action]

  sealed trait Action
  object Action {
    case object Continue extends Action
    case object Exit     extends Action

    val empty: Action = Continue
  }

  def isContinue(a: Action): Boolean =
    a == Action.Continue

  def isExit(a: Action): Boolean =
    a == Action.Exit

  def mergeActions(lhs: Action, rhs: Action): Action =
    (lhs, rhs) match {
      case (Action.Exit, _) => Action.Exit
      case (_, Action.Exit) => Action.Exit
      case _                => Action.Continue
    }

  def withDefaultHandler(handler: KeySeqHandler): KeySeqHandler = (ks: List[KeySeq]) =>
    for {
      uRes <- handler(ks)
      dRes <- defaultHandler(ks)
    } yield EventLoop.mergeActions(uRes, dRes)

  val defaultHandler: KeySeqHandler = (ks: List[KeySeq]) => {
    val initialState: EventLoop.Action = Action.Continue
    val res = ks.foldLeft(initialState) { (acc, k) =>
      val act =
        if (k.isEsc)
          Action.Exit
        else
          Action.Continue

      EventLoop.mergeActions(act, acc)
    }
    Right(res)
  }

  val continueHandler: KeySeqHandler = (_: List[KeySeq]) => Right(Action.Continue)
}
