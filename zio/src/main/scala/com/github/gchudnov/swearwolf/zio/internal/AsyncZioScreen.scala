package com.github.gchudnov.swearwolf.zio.internal

import com.github.gchudnov.swearwolf.term.AsyncScreen
import com.github.gchudnov.swearwolf.term.AsyncTerm
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Size
import zio.*

final class AsyncZioScreen(term: AsyncTerm[Task]) extends AsyncScreen[Task](term):

  override def size: Task[Option[Size]] = ???

  override def close(): Task[Unit] = ???
