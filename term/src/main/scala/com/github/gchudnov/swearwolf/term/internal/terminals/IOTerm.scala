package com.github.gchudnov.swearwolf.term.internal.terminals

import java.io.OutputStream
import java.io.InputStream

/**
 * Utility methods for StdIn / StdOut.
 */
private[term] object IOTerm:

  def nextChunk(in: InputStream, maxLen: Int): Option[Array[Byte]] =
    val data = new Array[Byte](maxLen)
    val n    = in.read(data, 0, data.length)
    n match
      case n if n > 0 =>
        Some(data.take(n))
      case -1 =>
        None // EOF
      case 0 =>
        Some(Array.empty[Byte])
      case x =>
        sys.error(s"Unexpected number of bytes were read: $x")

  def nextAvailableChunk(in: InputStream): Option[Array[Byte]] =
    val nAvail = in.available()
    if nAvail > 0 then nextChunk(in, nAvail)
    else Some(Array.emptyByteArray)
