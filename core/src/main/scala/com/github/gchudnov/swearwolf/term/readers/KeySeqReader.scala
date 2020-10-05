package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.KeySeq

private[term] trait KeySeqReader {
  /**
   * Reads a sequence of bytes and returns parsed key sequence and the rest of the bytes.
   * @param data bytes to read.
   * @return parsed key sequence and the rest of the bytes.
   */
  def read(data: Seq[Byte]): (KeySeq, Seq[Byte])
}
