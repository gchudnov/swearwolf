package com.github.gchudnov.swearwolf.term.internal.readers

import com.github.gchudnov.swearwolf.term.internal.ReadState
import com.github.gchudnov.swearwolf.util.bytes.Bytes

private[internal] trait KeySeqReader:

  /**
   * Reads a sequence of bytes and returns parsed key sequence and the rest of the bytes.
   * @param data
   *   bytes to read.
   * @return
   *   ReadState (parsed key sequence and the rest of the data)
   */
  def read(data: Bytes): ReadState
