package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.KeySeq

private[swearwolf] sealed trait ReadState

private[swearwolf] final case class PartialReadState(rest: Seq[Byte])                extends ReadState
private[swearwolf] final case class UnknownReadState(rest: Seq[Byte])                extends ReadState
private[swearwolf] final case class ParsedReadState(keqSeq: KeySeq, rest: Seq[Byte]) extends ReadState

private[swearwolf] object ReadState:
  val empty: ReadState = UnknownReadState(Seq.empty[Byte])
