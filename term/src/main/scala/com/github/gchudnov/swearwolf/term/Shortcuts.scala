package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.keys.KeyCode
import scala.collection.immutable.Seq

/**
 * Keyboard Shortcuts represented as a String
 */
object Shortcuts:

  private val keys = Map[KeyCode, String](
    KeyCode.Tab       -> "⇥",
    KeyCode.Enter     -> "↵",
    KeyCode.Esc       -> "ESC",
    KeyCode.Space     -> "SPACE",
    KeyCode.Backspace -> "⇤",
    KeyCode.Up        -> "↑",
    KeyCode.Down      -> "↓",
    KeyCode.Left      -> "←",
    KeyCode.Right     -> "→",
    KeyCode.Shift     -> "⇧",
    KeyCode.Alt       -> "ALT",
    KeyCode.Control   -> "CTRL",
  )

  def keyCombination(ks: Seq[KeyCode]): String =
    ks.map(keyCode).mkString(" + ")

  def keyCode(key: KeyCode): String =
    keys.getOrElse(key, "?")
