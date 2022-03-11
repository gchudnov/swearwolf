package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.MonadError

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*
import com.github.gchudnov.swearwolf.util.func.Monoid
import com.github.gchudnov.swearwolf.term.internal.Reader
import com.github.gchudnov.swearwolf.util.bytes.Bytes

abstract class SyncEventLoop[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyEventLoop[F](term) {}

