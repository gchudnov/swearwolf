package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.bytes.Bytes

private[term] sealed trait ReadState

private[term] final case class PartialReadState(rest: Bytes)                extends ReadState
private[term] final case class UnknownReadState(rest: Bytes)                extends ReadState
private[term] final case class ParsedReadState(keqSeq: KeySeq, rest: Bytes) extends ReadState

private[term] object ReadState:
  val empty: ReadState = UnknownReadState(Bytes.empty)
