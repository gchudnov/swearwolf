package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.{Point, Size}

trait KeySeq

/**
 * Marks that a KeySeq was partially parsed and no data bytes were consumed.
 * Parsing should be resumed when new data is received.
 */
private[swearwolf] case object UnknownKeySeq extends KeySeq

case object PartialKeySeq extends KeySeq

final case class SizeKeySeq(sz: Size)                                                                                              extends KeySeq
final case class CharKeySeq(ch: Char, mods: Set[KeyModifier] = Set.empty[KeyModifier])                                             extends KeySeq
final case class CtrlKeySeq(key: KeyCode, mods: Set[KeyModifier] = Set.empty[KeyModifier])                                         extends KeySeq
final case class MouseKeySeq(pt: Point, button: MouseButton, action: MouseAction, mods: Set[KeyModifier] = Set.empty[KeyModifier]) extends KeySeq
final case class UnfamiliarKeySeq(bytes: Seq[Byte])                                                                                extends KeySeq
