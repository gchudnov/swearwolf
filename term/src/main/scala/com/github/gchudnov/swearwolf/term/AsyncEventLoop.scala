package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError

abstract class AsyncEventLoop[F[_]](term: Term[F])(using ME: MonadError[F]) extends AnyEventLoop[F](term) {}
