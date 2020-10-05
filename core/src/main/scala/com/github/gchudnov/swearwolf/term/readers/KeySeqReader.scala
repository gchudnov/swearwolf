package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term.KeySeq

private[term] trait KeySeqReader {
  def read(data: Seq[Byte]): (KeySeq, Seq[Byte])
}
