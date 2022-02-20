package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }

sealed trait KeySeq

final case class SizeKeySeq(sz: Size)                                                                                              extends KeySeq
final case class CharKeySeq(ch: Char, mods: Set[KeyModifier] = Set.empty[KeyModifier])                                             extends KeySeq
final case class CtrlKeySeq(key: KeyCode, mods: Set[KeyModifier] = Set.empty[KeyModifier])                                         extends KeySeq
final case class MouseKeySeq(pt: Point, button: MouseButton, action: MouseAction, mods: Set[KeyModifier] = Set.empty[KeyModifier]) extends KeySeq
final case class UnknownKeySeq(bytes: Seq[Byte])                                                                                   extends KeySeq
