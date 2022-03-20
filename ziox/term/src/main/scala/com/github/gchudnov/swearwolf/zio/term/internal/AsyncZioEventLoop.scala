package com.github.gchudnov.swearwolf.zio.term.internal

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.eventloops.{ AnyEventLoop, AsyncEventLoop }
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.terms.AsyncTerm
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*
import zio.stream.*

/**
 * ZIO Async Event Loop
 */
private[term] final class AsyncZioEventLoop(term: AsyncTerm[Task]) extends AsyncEventLoop[Task](term):
  import AnyEventLoop.*

  override def run(handler: KeySeqHandler[Task]): Task[Unit] =
    stream(handler).runDrain

  private def stream(handler: KeySeqHandler[Task]): ZStream[Any, Throwable, KeySeq] =
    ZStream
      .unfoldZIO(Acc.empty)(acc =>
        iterate(acc).flatMap {
          case None =>
            ZIO.none
          case Some((key, value)) =>
            handler(key).map {
              case EventLoop.Action.Continue =>
                Some((key, value))
              case _ =>
                None
            }
        }
      )
