package com.github.gchudnov.swearwolf.term.internal.readers

import com.github.gchudnov.swearwolf.term.keys.CharKeySeq
import com.github.gchudnov.swearwolf.term.internal.{ ParsedReadState, ReadState, UnknownReadState }
import com.github.gchudnov.swearwolf.util.bytes.Bytes

/**
 * Reads a character sequence.
 */
private[internal] object CharReader extends BasicKeySeqReader:

  override def read(data: Bytes): ReadState =
    if data.isEmpty then UnknownReadState(data)
    else
      val k    = data.head
      val rest = data.tail

      if isPrintable(k) then ParsedReadState(CharKeySeq(k.toChar), rest)
      else UnknownReadState(data)
