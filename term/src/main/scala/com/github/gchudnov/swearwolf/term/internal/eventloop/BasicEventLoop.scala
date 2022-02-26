package com.github.gchudnov.swearwolf.term.internal.eventloop

import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.keys.KeySeq

private[term] final class BasicEventLoop(handler: KeySeqHandler) extends EventLoop:
  override def run(): Unit =
    ???
  //   while true do
  //     val event = poll()
  //     if event.isDefined then event.get.handle()

  override def poll(): Option[KeySeq] =
    ???
  //   if events.isEmpty then None else Some(events.dequeue())

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

while (true)
{
  processInput();
  update();
  render();
}

 */
