package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term._

/**
 * Reads a character sequence.
 */
object CharReader extends BasicKeySeqReader {

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
