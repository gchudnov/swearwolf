package com.github.gchudnov.swearwolf.zio.internal

import com.github.gchudnov.swearwolf.zio.internal.RIOMonadAsyncError
import com.github.gchudnov.swearwolf.term.AsyncTerm
import java.io.InputStream
import java.io.OutputStream
import zio.*

/**
 * ZIO Terminal
 */
final class AsyncZioTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends AsyncTerm[Task](in = in, out = out, isClose = isClose)(new RIOMonadAsyncError[Any]) {}
