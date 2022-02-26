package com.github.gchudnov.swearwolf.term.internal.eventloop

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax

import java.util.concurrent.ConcurrentLinkedQueue
import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

private[term] final class TermEventLoop(term: Term, handler: KeySeqHandler) extends EventLoop:
  import TermEventLoop.*

  private val ks          = ConcurrentLinkedQueue[KeySeq]
  private val loopHandler = exitHandler | handler

  override def run(): Either[Throwable, Unit] =
    @tailrec
    def iterate(): Either[Throwable, Unit] =
      val errOrKeySeq = poll()
      errOrKeySeq match
        case Left(err) =>
          Left(err)
        case Right(keqSeq) =>
          keqSeq match
            case Some(keq) =>
              val errOrAction = loopHandler(keq)
              errOrAction match
                case Left(err) =>
                  Left(err)
                case Right(a) if a.isContinue =>
                  iterate()
                case _ =>
                  Right(())
            case None =>
              iterate()

    iterate()

  override def poll(): Either[Throwable, Option[KeySeq]] =
    if (ks.isEmpty) then consumeAndEnqueue().map(_ => dequeue())
    else Right(dequeue())

  /**
   * Consume KeySeq from the terminal and put them to the queue
   */
  private def consumeAndEnqueue(): Either[Throwable, Unit] =
    term.blockingPoll() match
      case Left(err) =>
        Left(err)
      case Right(keys) =>
        ks.addAll(keys.asJava)
        Right(())

  private def dequeue(): Option[KeySeq] =
    if (ks.isEmpty) then None
    else Some(ks.poll())

object TermEventLoop:
  import KeySeqSyntax.*

  val exitHandler: KeySeqHandler = (ks: KeySeq) =>
    if ks.isEsc then Right(EventLoop.Action.Exit)
    else Right(EventLoop.Action.Continue)