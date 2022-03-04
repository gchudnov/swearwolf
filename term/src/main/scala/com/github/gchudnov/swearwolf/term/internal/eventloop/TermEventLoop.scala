package com.github.gchudnov.swearwolf.term.internal.eventloop

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.util.func.MonadError

import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*
import com.github.gchudnov.swearwolf.util.func.Monoid

private[eventloop] final class TermEventLoop[F[_]: MonadError](term: Term[F]) extends EventLoop[F]:
  import TermEventLoop.*
  import EventLoop.*
  import KeySeqSyntax.*
  import MonadError.*

  private val bytes = ConcurrentLinkedDeque[Byte]
  private val keys = ConcurrentLinkedQueue[KeySeq]

  private val exitHandler: KeySeqHandler[F] = (ks: KeySeq) =>
    val action = if ks.isEsc then 
      EventLoop.Action.Exit
    else 
      EventLoop.Action.Continue
      
    summon[MonadError[F]].unit(action)

  override def run(handler: KeySeqHandler[F]): F[Unit] =
    val loopHandler = exitHandler | handler

    // summon[Monoid[KeySeqHandler[F]]].combine(loopHandler, loopHandler)

    @tailrec
    def iterate(): F[Unit] =
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

  private def poll(): F[Option[KeySeq]] =

    @tailrec
    def iterate(): F[Option[KeySeq]] =
      readTermAndOffer() match
        case Left(ex) =>
          Left(ex)
        case Right(maybeN) =>
          maybeN match
            case Some(n) if n > 0 =>
              Right(Some(keys.poll()))
            case Some(n) if n == 0 =>
              iterate()
            case _ =>
              Right(None)

    if keys.isEmpty then iterate()
    else Right(Some(keys.poll()))

  /**
   * Read KeySeq from the Terminal and put them to the Event-Loop queue
   *
   * returns None, if EOF is reached
   */
  private def readTermAndOffer(): F[Option[Int]] =
    for {
      maybeBytes <- term.read()
      keySeqs <- maybeBytes match {
        case Some(bytes) =>
          ???
        case None =>
          ???
      }
    } yield ()

    ???


    // for
    //   maybeKs <- term.blockingPoll()
    //   maybeN = maybeKs.map(ks =>
    //              keys.addAll(ks.asJava)
    //              ks.size
    //            )
    // yield maybeN

  private def bytesToKeySeqs(bytes: Array[Byte]): Array[KeySeq] =
    ???

private[term] object TermEventLoop:
  import KeySeqSyntax.*

  // def make(term: Term): TermEventLoop =
  //   new TermEventLoop(term)
