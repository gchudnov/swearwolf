package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term.ReadState

private[term] trait KeySeqReader {

  /**
   * Reads a sequence of bytes and returns parsed key sequence and the rest of the bytes.
   * @param data bytes to read.
   * @return ReadState (parsed key sequence and the rest of the data)
   */
  def read(data: Seq[Byte]): ReadState

}
