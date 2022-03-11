package com.github.gchudnov.swearwolf.zio

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.internal.screens.ShellScreen
import com.github.gchudnov.swearwolf.zio.internal.AsyncZioScreen
import com.github.gchudnov.swearwolf.zio.internal.AsyncZioTerm
import com.github.gchudnov.swearwolf.zio.internal.RIOMonadAsyncError
import sun.misc.Signal
import sun.misc.SignalHandler
import zio.*

object ZioScreen:

  def shellLayer: RLayer[AsyncZioTerm, AsyncZioScreen] =
    (for
      term             <- ZIO.service[AsyncZioTerm]
      cb: SignalHandler = (sig: Signal) => ()



      pairs             = ShellScreen.initRollbackActions[Task](cb)
      cleanup          <- ShellScreen.initTerm(term, pairs)
      screen            = new AsyncZioScreen(term, cleanup)
    yield (screen)).toLayer

    // TODO: ^^^ the impl is very rough, fix it
    // TODO: cb should call size, flush on the screen


/*
// Asynchronous Callback-based API
def registerCallback(
    name: String,
    onEvent: Int => Unit,
    onError: Throwable => Unit
): Unit = ???

// Lifting an Asynchronous API to ZStream
val stream = ZStream.async[Any, Throwable, Int] { cb =>
  registerCallback(
    "foo",
    event => cb(ZIO.succeed(Chunk(event))),
    error => cb(ZIO.fail(error).mapError(Some(_)))
  )
}

ZIO

  def async[R, E, A](
    register: (ZIO[R, E, A] => Unit) => Any,
    blockingOn: => FiberId = FiberId.None
  )(implicit trace: ZTraceElement): ZIO[R, E, A] =

  def asyncMaybe[R, E, A](
    register: (ZIO[R, E, A] => Unit) => Option[ZIO[R, E, A]],
    blockingOn: => FiberId = FiberId.None
  )(implicit trace: ZTraceElement): ZIO[R, E, A] =
*/