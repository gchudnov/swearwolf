package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.eventloops.AnyEventLoop
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.{EventLoop, Term}
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.{MonadError, Monoid}

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

abstract class SyncEventLoop[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyEventLoop[F](term):
  import AnyEventLoop.*
  import MonadError.*

  override def run(handler: KeySeqHandler[F]): F[Unit] =
    import KeySeq.*

    ME.tailRecM(Acc.empty)(acc => {
      iterate(acc).flatMap {
        case None =>
          ME.succeed(Right(()))
        case Some(ks: KeySeq, xAcc: Acc) =>
          handler(ks).map {
            case action if action == EventLoop.Action.Continue =>
              Left(xAcc)
            case _ =>
              Right(())
          }
      }
    })
