package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.{KeyCode, KeyModifier}
import com.github.gchudnov.swearwolf.term._

/**
 * Reads Ctrl + {KEY} sequences.
 */
private[term] object CtrlReader extends BasicKeySeqReader {

  val ctrlMap: Map[Byte, Byte] = Map(
    0x00.toByte -> ' '.toByte, // ^@
    0x1c.toByte -> '\\'.toByte, // ^\
    0x1d.toByte -> ']'.toByte, // ^]
    0x1e.toByte -> '^'.toByte, // ^^
    0x1f.toByte -> '_'.toByte // ^_
  )

  override def read(data: Seq[Byte]): (KeySeq, Seq[Byte]) =
    if (data.isEmpty)
      (UnknownKeySeq, data)
    else {
      val k    = data.head
      val rest = data.tail

      if (isControl(k))
        if (isEsc(k))
          if (rest.isEmpty || isEsc(rest.head))
            (CtrlKeySeq(KeyCode.Esc, Set.empty[KeyModifier]), rest)
          else if (rest.size == 1) {
            val k2    = rest.head
            val rest2 = rest.tail
            if (isControl(k2))
              (CharKeySeq(modifiedChar(k2).toChar, Set(KeyModifier.Ctrl, KeyModifier.Alt)), rest2)
            else if (isPrintable(k2))
              (CharKeySeq(k2.toChar, Set(KeyModifier.Alt)), rest2)
            else
              (UnknownKeySeq, data)
          } else
            (UnknownKeySeq, data)
        else if (isBackspace(k))
          (CtrlKeySeq(KeyCode.Backspace, Set.empty[KeyModifier]), rest)
        else if (isEnter(k))
          (CtrlKeySeq(KeyCode.Enter, Set.empty[KeyModifier]), rest)
        else if (isTab(k))
          (CtrlKeySeq(KeyCode.Tab, Set.empty[KeyModifier]), rest)
        else
          (CharKeySeq(modifiedChar(k).toChar, Set(KeyModifier.Ctrl)), rest)
      else
        (UnknownKeySeq, data)
    }

  private def modifiedChar(k: Byte): Byte = {
    val defaultValue = ('a'.toInt - 1 + k).toByte
    ctrlMap.getOrElse(k, defaultValue)
  }
}
