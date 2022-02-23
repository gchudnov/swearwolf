package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.KeySeq
import com.github.gchudnov.swearwolf.util.bytes.Bytes

private[swearwolf] sealed trait ReadState

private[swearwolf] final case class PartialReadState(rest: Bytes)                extends ReadState
private[swearwolf] final case class UnknownReadState(rest: Bytes)                extends ReadState
private[swearwolf] final case class ParsedReadState(keqSeq: KeySeq, rest: Bytes) extends ReadState

private[swearwolf] object ReadState:
  val empty: ReadState = UnknownReadState(Bytes.empty)
