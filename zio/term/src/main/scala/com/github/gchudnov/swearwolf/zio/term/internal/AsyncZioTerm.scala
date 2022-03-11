package com.github.gchudnov.swearwolf.zio.term.internal

import com.github.gchudnov.swearwolf.term.AsyncTerm
import com.github.gchudnov.swearwolf.zio.util.func.RIOMonadAsyncError
import zio.*

import java.io.InputStream
import java.io.OutputStream

/**
 * ZIO Terminal
 */
final class AsyncZioTerm(in: InputStream, out: OutputStream, isClose: Boolean) extends AsyncTerm[Task](in = in, out = out, isClose = isClose) {}
