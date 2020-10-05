package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.{KeyCode, KeyModifier, MouseAction, MouseButton}
import com.github.gchudnov.swearwolf.util.{Point, Size}

trait KeySeq

case object UnknownKeySeq extends KeySeq
case object PartialKeySeq extends KeySeq

final case class SizeKeySeq(sz: Size)                                                                                              extends KeySeq
final case class CharKeySeq(ch: Char, mods: Set[KeyModifier] = Set.empty[KeyModifier])                                             extends KeySeq
final case class CtrlKeySeq(key: KeyCode, mods: Set[KeyModifier] = Set.empty[KeyModifier])                                         extends KeySeq
final case class MouseKeySeq(pt: Point, button: MouseButton, action: MouseAction, mods: Set[KeyModifier] = Set.empty[KeyModifier]) extends KeySeq
final case class UnfamiliarKeySeq(bytes: Seq[Byte])                                                                                extends KeySeq

object KeySeq {

  implicit class KeySeqOps(private val keqSeq: KeySeq) extends AnyVal {

    def size: Option[Size] = keqSeq match {
      case SizeKeySeq(sz) =>
        Some(sz)
      case _ =>
        None
    }

    def mods: Set[KeyModifier] = keqSeq match {
      case CharKeySeq(_, ms) =>
        ms
      case CtrlKeySeq(_, ms) =>
        ms
      case _ =>
        Set.empty[KeyModifier]
    }

    def char: Option[Char] = keqSeq match {
      case CharKeySeq(ch, _) =>
        Some(ch)
      case _ =>
        None
    }

    def keyCode: Option[KeyCode] = keqSeq match {
      case CtrlKeySeq(keyCode, _) =>
        Some(keyCode)
      case _ =>
        None
    }

    def bytes: Option[Seq[Byte]] = keqSeq match {
      case UnfamiliarKeySeq(bs) =>
        Some(bs)
      case _ =>
        None
    }

    def point: Option[Point] = keqSeq match {
      case MouseKeySeq(pt, _, _, _) =>
        Some(pt)
      case _ =>
        None
    }

    def isEsc: Boolean = keqSeq.keyCode.contains(KeyCode.Esc)

    def isSize: Boolean = keqSeq.isInstanceOf[SizeKeySeq]

    def isMouseAction: Boolean = keqSeq.isInstanceOf[MouseKeySeq]

    def isMousePress: Boolean = keqSeq match {
      case MouseKeySeq(_, _, action, _) =>
        action == MouseAction.Press
      case _ =>
        false
    }

    def isMouseRelease: Boolean = keqSeq match {
      case MouseKeySeq(_, _, action, _) =>
        action == MouseAction.Release
      case _ =>
        false
    }
  }

}
