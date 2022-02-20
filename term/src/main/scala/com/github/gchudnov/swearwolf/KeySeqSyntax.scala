package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }

class KeySeqOps(private val keqSeq: KeySeq) extends AnyVal:

  /**
   * Get Size from the key sequence.
   * @return
   *   Some(Size) if the key sequence is size one otherwise None.
   */
  def size: Option[Size] = keqSeq match
    case SizeKeySeq(sz) =>
      Some(sz)
    case _ =>
      None

  /**
   * Get KeyModifier out of key sequence.
   * @return
   *   non-empty Set[KeyModifier] if key sequence defines key modifiers, otherwise - an empty set.
   */
  def mods: Set[KeyModifier] = keqSeq match
    case CharKeySeq(_, ms) =>
      ms
    case CtrlKeySeq(_, ms) =>
      ms
    case _ =>
      Set.empty[KeyModifier]

  /**
   * Get a Char out of key sequence.
   * @return
   *   Some(Char) if the key sequence defines characters, otherwise None.
   */
  def char: Option[Char] = keqSeq match
    case CharKeySeq(ch, _) =>
      Some(ch)
    case _ =>
      None

  /**
   * Get KeyCode out of the key sequence.
   * @return
   *   Some(KeyCode) if the key sequence defines KeyCode, otherwise None.
   */
  def keyCode: Option[KeyCode] = keqSeq match
    case CtrlKeySeq(keyCode, _) =>
      Some(keyCode)
    case _ =>
      None

  /**
   * Gets a sequence of bytes out of the key sequence.
   * @return
   *   Some(Seq[Byte]) if the sequence provides them, otherwise None
   */
  def bytes: Option[Seq[Byte]] = keqSeq match
    case UnknownKeySeq(bs) =>
      Some(bs)
    case _ =>
      None

  /**
   * Get point out of the key sequence.
   * @return
   *   Some(Point) if key sequence provides point, otherwise None
   */
  def point: Option[Point] = keqSeq match
    case MouseKeySeq(pt, _, _, _) =>
      Some(pt)
    case _ =>
      None

  /**
   * Checks whether key sequence is an ESC.
   * @return
   *   True if ESC button was clicked, otherwise False.
   */
  def isEsc: Boolean = keyCode.contains(KeyCode.Esc)

  /**
   * Checks whether key sequence is Size-event.
   * @return
   *   True if key sequence is size event, otherwise False.
   */
  def isSize: Boolean = keqSeq.isInstanceOf[SizeKeySeq]

  /**
   * Checks if key sequence is a mouse action.
   * @return
   *   True if key sequence is a mouse action, otherwise False.
   */
  def isMouseAction: Boolean = keqSeq.isInstanceOf[MouseKeySeq]

  /**
   * Checks if key sequence is a mouse press event.
   * @return
   *   True if key sequence is a mouse press event, otherwise false.
   */
  def isMousePress: Boolean = keqSeq match
    case MouseKeySeq(_, _, action, _) =>
      action == MouseAction.Press
    case _ =>
      false

  /**
   * Checks if key sequence is a mouse release event.
   * @return
   *   True if key sequence is a mouse release event, otherwise false.
   */
  def isMouseRelease: Boolean = keqSeq match
    case MouseKeySeq(_, _, action, _) =>
      action == MouseAction.Release
    case _ =>
      false

trait KeySeqSyntax:
  implicit def keySeqOps(keqSeq: KeySeq): KeySeqOps = new KeySeqOps(keqSeq)

object KeySeqSyntax extends KeySeqSyntax
