package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.eventloops.AnyEventLoop
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.{ EventLoop, Term }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.func.{ MonadError, Monoid }

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

abstract class SyncEventLoop[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyEventLoop[F](term) {}