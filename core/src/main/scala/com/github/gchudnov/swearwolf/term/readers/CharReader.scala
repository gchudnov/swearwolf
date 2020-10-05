package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.{CharKeySeq, KeySeq, UnknownKeySeq}

/**
 * Reads a character sequence.
 */
private[term] object CharReader extends BasicKeySeqReader {

  override def read(data: Seq[Byte]): (KeySeq, Seq[Byte]) =
    if (data.isEmpty)
      (UnknownKeySeq, data)
    else {
      val k    = data.head
      val rest = data.tail

      if (isPrintable(k))
        (CharKeySeq(k.toChar), rest)
      else
        (UnknownKeySeq, data)
    }
}
