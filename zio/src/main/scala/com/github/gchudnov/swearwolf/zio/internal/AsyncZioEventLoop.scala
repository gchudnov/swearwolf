package com.github.gchudnov.swearwolf.zio.internal

import com.github.gchudnov.swearwolf.term.AnyEventLoop
import com.github.gchudnov.swearwolf.term.AsyncEventLoop
import com.github.gchudnov.swearwolf.term.AsyncTerm
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.zio.internal.RIOMonadAsyncError
import zio.*
import zio.stream.*

/**
 * ZIO Async Event Loop
 */
final class AsyncZioEventLoop(term: AsyncTerm[Task]) extends AsyncEventLoop[Task](term)(new RIOMonadAsyncError[Any]):
  import AnyEventLoop.*

  override def run(handler: KeySeqHandler[Task]): Task[Unit] =
    stream(handler).runDrain

  private def stream(handler: KeySeqHandler[Task]): ZStream[Any, Throwable, KeySeq] =
    ZStream
      .unfoldZIO(Acc.empty)(acc => {
        iterate(acc)
        .flatMap({
          case None =>
            ZIO.succeed(None)
          case Some((key, value)) =>
            handler(key).map {
              case EventLoop.Action.Continue =>
                Some((key, value))
              case _ =>
                None
            }
        })
      })
