package com.github.gchudnov.swearwolf.term.internal

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.bytes.Bytes

private[internal] sealed trait ReadState

private[internal] final case class PartialReadState(rest: Bytes)                extends ReadState
private[internal] final case class UnknownReadState(rest: Bytes)                extends ReadState
private[internal] final case class ParsedReadState(keqSeq: KeySeq, rest: Bytes) extends ReadState

private[internal] object ReadState:
  val empty: ReadState = 
    UnknownReadState(Bytes.empty)
