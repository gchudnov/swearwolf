package com.github.gchudnov.swearwolf.term.internal.eventloop

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax

import java.util.concurrent.ConcurrentLinkedQueue
import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

private[eventloop] final class TermEventLoop(term: Term) extends EventLoop:
  import TermEventLoop.*

  private val q = ConcurrentLinkedQueue[KeySeq]

  override def run(handler: KeySeqHandler): Either[Throwable, Unit] =
    val loopHandler = exitHandler | handler

    @tailrec
    def iterate(): Either[Throwable, Unit] =
      val errOrKeySeq = poll()
      errOrKeySeq match
        case Left(err) =>
          Left(err)
        case Right(maybeKeySeq) =>
          maybeKeySeq match
            case Some(keySeq) =>
              val errOrAction = loopHandler(keySeq)
              errOrAction match
                case Left(err) =>
                  Left(err)
                case Right(a) if a.isContinue =>
                  iterate()
                case _ =>
                  Right(()) // Quit signal from the loopHandler
            case None =>
              Right(()) // EOF is reached

    iterate()

  override def poll(): Either[Throwable, Option[KeySeq]] =

    def iterate(): Either[Throwable, Option[KeySeq]] =
      readTermAndOffer().flatMap(maybeOk =>
        maybeOk match
          case Some(n) if n > 0 =>
            Right(Some(q.poll()))
          case Some(n) if n == 0 =>
            iterate()
          case _ =>
            Right(None)
      )

    if q.isEmpty then iterate()
    else Right(Some(q.poll()))

  /**
   * Read KeySeq from the Terminal and put them to the Event-Loop queue
   *
   * returns None, if EOF is reached
   */
  private def readTermAndOffer(): Either[Throwable, Option[Int]] =
    for
      maybeKs <- term.blockingPoll()
      maybeN = maybeKs.map(ks =>
                 q.addAll(ks.asJava)
                 ks.size
               )
    yield maybeN

private[term] object TermEventLoop:
  import KeySeqSyntax.*

  val exitHandler: KeySeqHandler = (ks: KeySeq) =>
    if ks.isEsc then Right(EventLoop.Action.Exit)
    else Right(EventLoop.Action.Continue)

  def make(term: Term): TermEventLoop =
    new TermEventLoop(term)
