package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax

// TODO: trait with methods???

object EventLoop:
  import KeySeqSyntax.*

  type KeySeqHandler = List[KeySeq] => Either[Throwable, EventLoop.Action]

  sealed trait Action
  object Action:
    case object Continue extends Action
    case object Exit     extends Action

    val empty: Action =
      Continue

  def isContinue(a: Action): Boolean =
    a == Action.Continue

  def isExit(a: Action): Boolean =
    a == Action.Exit

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


/*

  @tailrec
  override def eventLoop(handler: KeySeqHandler): Either[Throwable, Unit] =
    val errOrAction = for
      ks     <- eventPoll()
      action <- handler(ks)
    yield action

    errOrAction match
      case Left(err)                                  => Left(err)
      case Right(action) if action == Action.Continue => eventLoop(handler)
      case _                                          => Right(())

  override def eventPoll(): Either[Throwable, List[KeySeq]] =
    for
      ks <- term.blockingPoll()
      _  <- internalStateHandler(ks)
    yield ks

  private def internalStateHandler(ks: List[KeySeq]): Either[Throwable, Unit] =
    for _ <- Right[Throwable, Unit](ks.foreach(trackScreenSize))
    yield ()
*/