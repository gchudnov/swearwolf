package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.util.func.MonadError

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.keys.KeySeqSyntax
import com.github.gchudnov.swearwolf.util.func.MonadError

import scala.annotation.tailrec
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.util.bytes.Bytes

/**
 * Any EventLoop
 */
abstract class AnyEventLoop[F[_]](term: Term[F])(implicit ME: MonadError[F]) extends EventLoop[F]:
  import AnyEventLoop.*
  import EventLoop.*
  import KeySeqSyntax.*
  import MonadError.*

  /**
   * Reads the next KeySeq.
   *
   * A Blocking operation.
   *
   * Returns None if the input is closed, otherwise Some(KeySeq) and the accumulator that can be used to read the next KeySeq.
   */
  protected def iterate(acc: Acc): F[Option[(KeySeq, Acc)]] =
    acc match
      case Acc(ks, rest) if ks.nonEmpty =>
        ME.pure(Some(ks.head, Acc(ks.tail, rest)))
      case Acc(ks, rest) if ks.isEmpty =>
        val (xKs, xRest) = Reader.parseBytes(Bytes(rest))
        if (xKs.nonEmpty) then iterate(Acc(xKs, xRest.asSeq))
        else
          term.read().flatMap {
            case Some(bytes) =>
              val (yKs, yRest) = Reader.parseBytes(Bytes(bytes))
              iterate(Acc(yKs, yRest.asSeq))
            case None =>
              ME.pure(None)
          }

object AnyEventLoop:
  import KeySeqSyntax.*

  final case class Acc(ks: Seq[KeySeq], rest: Seq[Byte])

  object Acc:
    val empty = Acc(Seq.empty[KeySeq], Seq.empty[Byte])
