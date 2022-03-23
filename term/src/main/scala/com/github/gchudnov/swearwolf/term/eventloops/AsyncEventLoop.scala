package com.github.gchudnov.swearwolf.term.eventloops

import com.github.gchudnov.swearwolf.term.{ EventLoop, Term }
import com.github.gchudnov.swearwolf.term.eventloops.AnyEventLoop
import com.github.gchudnov.swearwolf.util.func.{ MonadAsyncError, MonadError }

abstract class AsyncEventLoop[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyEventLoop[F](term) {}
