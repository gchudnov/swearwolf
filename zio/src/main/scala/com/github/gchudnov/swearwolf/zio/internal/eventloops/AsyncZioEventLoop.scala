package com.github.gchudnov.swearwolf.zio.internal.eventloops

import com.github.gchudnov.swearwolf.term.AsyncEventLoop
import com.github.gchudnov.swearwolf.term.EventLoop
import zio.*

/**
 * ZIO Async Event Loop
 */
final class AsyncZioEventLoop(term: AsyncTerm[Task]) extends AsyncEventLoop[Task](term)(new RIOMonadAsyncError[Any]):

  override def run(handler: KeySeqHandler[Task]): Task[Unit] =
    pollOnce().mapZIO(handler).doWhile(_ == EventLoop.Action.Continue)
