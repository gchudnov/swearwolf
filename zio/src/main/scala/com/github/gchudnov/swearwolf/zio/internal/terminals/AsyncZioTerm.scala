package com.github.gchudnov.swearwolf.zio.internal.terminals

import com.github.gchudnov.swearwolf.zio.AsyncTerm
import java.io.InputStream
import java.io.OutputStream

/**
 * ZIO Terminal
 */
final class AsyncZioTerm(in: InputStream, out: OutputStream, isClose: Boolean)
    extends AsyncTerm[Task](in = in, out = out, isClose = isClose)(new RIOMonadAsyncError[Any]) {}
