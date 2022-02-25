package com.github.gchudnov.swearwolf.term.internal.readers

import com.github.gchudnov.swearwolf.term.{ ParsedReadState, ReadState, UnknownReadState }
import com.github.gchudnov.swearwolf.term.keys.{ CharKeySeq, CtrlKeySeq, KeyModifier }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.term.keys.KeyCode

/**
 * Reads Ctrl + {KEY} sequences.
 */
private[term] object CtrlReader extends BasicKeySeqReader:

  val ctrlMap: Map[Byte, Byte] = Map(
    0x00.toByte -> ' '.toByte,  // ^@
    0x1c.toByte -> '\\'.toByte, // ^\
    0x1d.toByte -> ']'.toByte,  // ^]
    0x1e.toByte -> '^'.toByte,  // ^^
    0x1f.toByte -> '_'.toByte   // ^_
  )

  override def read(data: Bytes): ReadState =
    if data.isEmpty then UnknownReadState(data)
    else {
      val k    = data.head
      val rest = data.tail

      if isControl(k) then
        if isEsc(k) then
          if rest.isEmpty || isEsc(rest.head) then ParsedReadState(CtrlKeySeq(KeyCode.Esc, Set.empty[KeyModifier]), rest)
          else if rest.size == 1 then
            val k2    = rest.head
            val rest2 = rest.tail
            if isControl(k2) then ParsedReadState(CharKeySeq(modifiedChar(k2).toChar, Set(KeyModifier.Ctrl, KeyModifier.Alt)), rest2)
            else if isPrintable(k2) then ParsedReadState(CharKeySeq(k2.toChar, Set(KeyModifier.Alt)), rest2)
            else UnknownReadState(data)
          else UnknownReadState(data)
        else if isBackspace(k) then ParsedReadState(CtrlKeySeq(KeyCode.Backspace, Set.empty[KeyModifier]), rest)
        else if isEnter(k) then ParsedReadState(CtrlKeySeq(KeyCode.Enter, Set.empty[KeyModifier]), rest)
        else if isTab(k) then ParsedReadState(CtrlKeySeq(KeyCode.Tab, Set.empty[KeyModifier]), rest)
        else ParsedReadState(CharKeySeq(modifiedChar(k).toChar, Set(KeyModifier.Ctrl)), rest)
      else UnknownReadState(data)
    }

  private def modifiedChar(k: Byte): Byte =
    val defaultValue = ('a'.toInt - 1 + k).toByte
    ctrlMap.getOrElse(k, defaultValue)
