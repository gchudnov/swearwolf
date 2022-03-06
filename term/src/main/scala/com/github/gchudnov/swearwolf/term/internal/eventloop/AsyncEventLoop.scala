package com.github.gchudnov.swearwolf.term.internal.eventloop

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError

import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.ConcurrentLinkedQueue
import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*
import com.github.gchudnov.swearwolf.util.func.Monoid
import scala.collection.mutable.ListBuffer
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.util.bytes.Bytes

// TODO: make MonadError as implicit | NOTE: this class is Async

abstract class AsyncEventLoop[F[_]](term: Term[F])(implicit val monad: MonadAsyncError[F]) extends EventLoop[F]:
  import AsyncEventLoop.*
  import EventLoop.*
  import KeySeqSyntax.*
  import MonadError.*

  private val byteQueue = ConcurrentLinkedDeque[Byte]
  private val keyQueue = ConcurrentLinkedQueue[KeySeq]

  private val exitHandler: KeySeqHandler[F] = (ks: KeySeq) =>
    val action = if ks.isEsc then 
      EventLoop.Action.Exit
    else 
      EventLoop.Action.Continue
    monad.unit(action)

  // type KeySeqHandler[F[_]] = KeySeq => F[EventLoop.Action]

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
  */

  // TODO: change to runOnce ?
  // pollOnce(running, consumer).doWhile(_ == true).forkDaemon

  override def run(handler: KeySeqHandler[F]): F[Unit] =
    val loopHandler: KeySeqHandler[F] = exitHandler | handler

    @tailrec
    def iterate(): F[Unit] =
      val keySeq = pollNextKeySeq()

      ???

    ??? // TODO: not possible??

    // // OLD CODE
    // @tailrec
    // def iterate(): F[Unit] =
    //   val errOrKeySeq = poll()
    //   errOrKeySeq match
    //     case Left(err) =>
    //       Left(err)
    //     case Right(maybeKeySeq) =>
    //       maybeKeySeq match
    //         case Some(keySeq) =>
    //           val errOrAction = loopHandler(keySeq)
    //           errOrAction match
    //             case Left(err) =>
    //               Left(err)
    //             case Right(a) if a.isContinue =>
    //               iterate()
    //             case _ =>
    //               Right(()) // Quit signal from the loopHandler
    //         case None =>
    //           Right(()) // EOF is reached

    // iterate()

  /**
   * Returns the next KeySeq either from the queue or by reading from the terminal.
   */
  private def pollNextKeySeq(): F[Option[KeySeq]] =
    ???

    // // OLD CODE
    // @tailrec
    // def iterate(): F[Option[KeySeq]] =
    //   readTermAndOffer() match
    //     case Left(ex) =>
    //       Left(ex)
    //     case Right(maybeN) =>
    //       maybeN match
    //         case Some(n) if n > 0 =>
    //           Right(Some(keys.poll()))
    //         case Some(n) if n == 0 =>
    //           iterate()
    //         case _ =>
    //           Right(None)

    // if keys.isEmpty then iterate()
    // else Right(Some(keys.poll()))

  /**
   * Read next chunk of Bytes from the Term and insert them into the bytes queue.
   *
   * returns Some(n) when n bytes were read OR None, if EOF is reached
   */
  private def readNextBytes(): F[Option[Int]] =
    for {
      maybeBytes <- term.read()
      // TODO: parse and enqueue immediately KeySeq
      n = maybeBytes match {
        case Some(bytes) =>
          bytes.foreach(byteQueue.add)
          Some(bytes.size)
        case None =>
          None
      }
    } yield n

  /**
   * Pump Bytes from the ByteQueue into the KeySeqQueue.
   */
  private def pumpBytesToKeySeq(): F[Unit] =
    val bytes = dequeue(byteQueue)
    val (ks, rest) = Reader.parseBytes(Bytes(bytes.toArray))
    prepend(byteQueue, rest.toArray)
    // TODO: impl, no return here
    monad.unit(ks.toList)

  private def dequeue[T](q: ConcurrentLinkedDeque[T]): List[T] =
    @tailrec
    def iterate(acc: ListBuffer[T]): List[T] =
      val it = Option(q.pollFirst())
      it match
        case Some(x) =>
          acc += x
          iterate(acc)
        case None =>
          acc.toList    

    iterate(ListBuffer.empty[T])

  private def prepend[T](q: ConcurrentLinkedDeque[T], xs: Seq[T]): Unit =
    xs.reverse.foreach(q.addFirst)


private[term] object AsyncEventLoop:
  import KeySeqSyntax.*

  // def make(term: Term): TermEventLoop =
  //   new TermEventLoop(term)
