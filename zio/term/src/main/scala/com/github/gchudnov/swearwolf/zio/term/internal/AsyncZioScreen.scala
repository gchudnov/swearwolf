package com.github.gchudnov.swearwolf.zio.term.internal

import com.github.gchudnov.swearwolf.term.AsyncScreen
import com.github.gchudnov.swearwolf.term.AsyncTerm
import com.github.gchudnov.swearwolf.util.func.MonadAsyncError
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.term.internal.screens.TermAction
import com.github.gchudnov.swearwolf.util.geometry.Size
import zio.*

final class AsyncZioScreen(term: AsyncTerm[Task], cleanup: TermAction[Task]) extends AsyncScreen[Task](term):

  override def close(): Task[Unit] =
    cleanup(term)
      .flatMap(_ => term.close())