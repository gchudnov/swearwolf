package com.github.gchudnov.swearwolf.term.internal.eventloops

import com.github.gchudnov.swearwolf.term.SyncEventLoop
import com.github.gchudnov.swearwolf.term.AnyEventLoop
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import scala.annotation.tailrec

/**
 * Synchronous EventLoop.
 */
final class EitherEventLoop(term: Term[Either[Throwable, *]]) extends SyncEventLoop[Either[Throwable, *]](term)(EitherMonad):
  import AnyEventLoop.*

  override def run(handler: KeySeqHandler[Either[Throwable, *]]): Either[Throwable, Unit] =

    @tailrec
    def loop(acc: Acc): Either[Throwable, Unit] =
      iterate(acc) match
        case Left(t) => Left(t)
        case Right(Some(ks: KeySeq, xAcc: Acc)) =>
          handler(ks) match
            case Left(t) => Left(t)
            case Right(action) if action == EventLoop.Action.Continue =>
              loop(xAcc)
            case _ =>
              Right(())
        case Right(None) =>
          Right(())

          loop(acc)

    loop(Acc.empty)
