package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.CharKeySeq
import com.github.gchudnov.swearwolf.term.{ ParsedReadState, ReadState, UnknownReadState }

/**
 * Reads a character sequence.
 */
private[term] object CharReader extends BasicKeySeqReader:

  override def read(data: Seq[Byte]): ReadState =
    if data.isEmpty then UnknownReadState(data)
    else {
      val k    = data.head
      val rest = data.tail

      if isPrintable(k) then ParsedReadState(CharKeySeq(k.toChar), rest)
      else UnknownReadState(data)
    }
