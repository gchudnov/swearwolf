package com.github.gchudnov.swearwolf.zio.internal.terminals

import com.github.gchudnov.swearwolf.zio.AsyncTerm

/**
 * ZIO Terminal
 */
final class AsyncZioTerm() extends AsyncTerm[Task](monad = new RIOMonadAsyncError[Any]) {
  
}
