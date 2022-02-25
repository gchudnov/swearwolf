package com.github.gchudnov.swearwolf.term.keys

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.term.keys.KeyCode
import com.github.gchudnov.swearwolf.term.MouseButton
import com.github.gchudnov.swearwolf.term.MouseAction

sealed trait KeySeq

final case class SizeKeySeq(sz: Size)                                                                                              extends KeySeq
final case class CharKeySeq(ch: Char, mods: Set[KeyModifier] = Set.empty[KeyModifier])                                             extends KeySeq
final case class CtrlKeySeq(key: KeyCode, mods: Set[KeyModifier] = Set.empty[KeyModifier])                                         extends KeySeq
final case class MouseKeySeq(pt: Point, button: MouseButton, action: MouseAction, mods: Set[KeyModifier] = Set.empty[KeyModifier]) extends KeySeq
final case class UnknownKeySeq(bytes: Bytes)                                                                                   extends KeySeq
