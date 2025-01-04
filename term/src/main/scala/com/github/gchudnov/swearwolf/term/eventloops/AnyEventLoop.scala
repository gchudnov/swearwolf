package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.{ EventLoop, Term }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.MonadError

import scala.annotation.tailrec
import scala.collection.immutable.Seq

/**
 * Any EventLoop
 */
abstract class AnyEventLoop[F[_]](term: Term[F])(using ME: MonadError[F]) extends EventLoop[F]:
  import AnyEventLoop.*
  import EventLoop.*
  import KeySeq.*
  import MonadError.*

  override def run(handler: KeySeqHandler[F]): F[Unit] =
    import KeySeq.*

    ME.tailRecM(Acc.empty)(acc =>
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
    )

  /**
   * Reads the next KeySeq.
   *
   * A Blocking operation.
   *
   * Returns None if the input is closed, otherwise Some(KeySeq, Acc) - the accumulator that can be used to read the next KeySeq.
   */
  protected def iterate(acc: Acc): F[Option[(KeySeq, Acc)]] =
    acc match
      case Acc(ks, rest) if ks.nonEmpty =>
        ME.succeed(Some(ks.head, Acc(ks.tail, rest)))
      case Acc(ks, rest) if ks.isEmpty =>
        val (xKs, xRest) = Reader.parseBytes(Bytes(rest))
        if xKs.nonEmpty then iterate(Acc(xKs, xRest.asSeq))
        else
          term.read().flatMap {
            case Some(bytes) =>
              val (yKs, yRest) = Reader.parseBytes(Bytes(bytes))
              iterate(Acc(yKs, yRest.asSeq))
            case None =>
              ME.succeed(None)
          }

object AnyEventLoop:

  final case class Acc(ks: Seq[KeySeq], rest: Seq[Byte])

  object Acc:
    val empty: Acc = Acc(Seq.empty[KeySeq], Seq.empty[Byte])
