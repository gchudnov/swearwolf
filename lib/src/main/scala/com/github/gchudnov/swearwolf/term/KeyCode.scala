package com.github.gchudnov.swearwolf.term

sealed trait KeyCode

object KeyCode {

  case object Invalid            extends KeyCode // {Key Not Found}
  case object Nul                extends KeyCode // Null
  case object StartHeading       extends KeyCode // Start of Heading
  case object StartText          extends KeyCode // Start of Text
  case object EndText            extends KeyCode // End of Text
  case object EndTrans           extends KeyCode // End of Transmission
  case object Enquiry            extends KeyCode // Enquiry
  case object Ack                extends KeyCode // Acknowledge
  case object Bell               extends KeyCode // Bell
  case object Backspace          extends KeyCode // Backspace
  case object Tab                extends KeyCode // Horizontal Tab
  case object LineFeed           extends KeyCode // Line Feed
  case object VerticalTab        extends KeyCode // Vertical Tab
  case object FormFeed           extends KeyCode // Form Feed
  case object Enter              extends KeyCode // Carriage Return, Enter
  case object ShiftOut           extends KeyCode // Shift Out
  case object ShiftIn            extends KeyCode // Shift In
  case object Shift              extends KeyCode // Shift
  case object Control            extends KeyCode // Control
  case object Alt                extends KeyCode // Alt
  case object Pause              extends KeyCode // Pause
  case object CapsLock           extends KeyCode // CAPS LOCK
  case object Nack               extends KeyCode // Negative Acknowledge
  case object SyncIdle           extends KeyCode // Synchronous Idle
  case object EndTransBlock      extends KeyCode // End of Transmission Block
  case object Cancel             extends KeyCode // Cancel
  case object EndMedium          extends KeyCode // End of Medium
  case object Substitute         extends KeyCode // Substitute
  case object Esc                extends KeyCode // Escape
  case object FileSeparator      extends KeyCode // File Separator
  case object GroupSeparator     extends KeyCode // Group Separator
  case object RecordSeparator    extends KeyCode // Record Separator
  case object RUnitSeparator     extends KeyCode // Unit Separator
  case object Space              extends KeyCode // Space
  case object Exclamation        extends KeyCode // !
  case object Quote              extends KeyCode // "
  case object Sharp              extends KeyCode // #
  case object Dollar             extends KeyCode // $
  case object Percent            extends KeyCode // %
  case object Ampersand          extends KeyCode // &
  case object SingleQuote        extends KeyCode // '
  case object LeftRoundBracket   extends KeyCode // (
  case object RightRoundBracket  extends KeyCode // )
  case object Star               extends KeyCode // *
  case object Plus               extends KeyCode // +
  case object Comma              extends KeyCode // ,
  case object Minus              extends KeyCode // -
  case object Dot                extends KeyCode // .
  case object Divide             extends KeyCode // /
  case object Num0               extends KeyCode // 0
  case object Num1               extends KeyCode // 1
  case object Num2               extends KeyCode // 2
  case object Num3               extends KeyCode // 3
  case object Num4               extends KeyCode // 4
  case object Num5               extends KeyCode // 5
  case object Num6               extends KeyCode // 6
  case object Num7               extends KeyCode // 7
  case object Num8               extends KeyCode // 8
  case object Num9               extends KeyCode // 9
  case object Colon              extends KeyCode // :
  case object Semicolon          extends KeyCode // ;
  case object LessThan           extends KeyCode // <
  case object Equal              extends KeyCode // =
  case object GreaterThan        extends KeyCode // >
  case object QuestionMark       extends KeyCode // ?
  case object At                 extends KeyCode // @
  case object UpperA             extends KeyCode // A
  case object UpperB             extends KeyCode // B
  case object UpperC             extends KeyCode // C
  case object UpperE             extends KeyCode // E
  case object UpperF             extends KeyCode // F
  case object UpperG             extends KeyCode // G
  case object UpperD             extends KeyCode // D
  case object UpperH             extends KeyCode // H
  case object UpperI             extends KeyCode // I
  case object UpperJ             extends KeyCode // J
  case object UpperK             extends KeyCode // K
  case object UpperL             extends KeyCode // L
  case object UpperM             extends KeyCode // M
  case object UpperN             extends KeyCode // N
  case object UpperO             extends KeyCode // O
  case object UpperP             extends KeyCode // P
  case object UpperQ             extends KeyCode // Q
  case object UpperR             extends KeyCode // R
  case object UpperS             extends KeyCode // S
  case object UpperT             extends KeyCode // T
  case object UpperU             extends KeyCode // U
  case object UpperV             extends KeyCode // V
  case object UpperW             extends KeyCode // W
  case object UpperX             extends KeyCode // X
  case object UpperY             extends KeyCode // Y
  case object UpperZ             extends KeyCode // Z
  case object LeftSquareBracket  extends KeyCode // [
  case object BackSlash          extends KeyCode // \
  case object RightSquareBracket extends KeyCode // ]
  case object Exponent           extends KeyCode // ^
  case object Underscore         extends KeyCode // _
  case object BackQuote          extends KeyCode // `
  case object LowerA             extends KeyCode // a
  case object LowerB             extends KeyCode // b
  case object LowerC             extends KeyCode // c
  case object LowerD             extends KeyCode // d
  case object LowerE             extends KeyCode // e
  case object LowerF             extends KeyCode // f
  case object LowerG             extends KeyCode // g
  case object LowerH             extends KeyCode // h
  case object LowerI             extends KeyCode // i
  case object LowerJ             extends KeyCode // j
  case object LowerK             extends KeyCode // k
  case object LowerL             extends KeyCode // l
  case object LowerM             extends KeyCode // m
  case object LowerN             extends KeyCode // n
  case object LowerO             extends KeyCode // o
  case object LowerP             extends KeyCode // p
  case object LowerQ             extends KeyCode // q
  case object LowerR             extends KeyCode // r
  case object LowerS             extends KeyCode // s
  case object LowerT             extends KeyCode // t
  case object LowerU             extends KeyCode // u
  case object LowerV             extends KeyCode // v
  case object LowerW             extends KeyCode // w
  case object LowerX             extends KeyCode // x
  case object LowerY             extends KeyCode // y
  case object LowerZ             extends KeyCode // z
  case object LeftCurlyBracket   extends KeyCode // {
  case object VerticalLine       extends KeyCode // |
  case object RightCurlyBracket  extends KeyCode // }
  case object Tilde              extends KeyCode // ~

  case object PageUp      extends KeyCode // PageUp
  case object PageDown    extends KeyCode // PageDown
  case object End         extends KeyCode // End
  case object Home        extends KeyCode // Home
  case object Left        extends KeyCode // Left Arrow
  case object Up          extends KeyCode // Up Arrow
  case object Right       extends KeyCode // Right Arrow
  case object Down        extends KeyCode // Down Arrow
  case object Select      extends KeyCode // Select
  case object Print       extends KeyCode // Print
  case object Execute     extends KeyCode // Execute
  case object PrintScreen extends KeyCode // Print Screen
  case object Insert      extends KeyCode // Insert
  case object Delete      extends KeyCode // Delete
  case object Help        extends KeyCode // Help

  case object LWin  extends KeyCode // Left Windows key
  case object RWin  extends KeyCode // Right Windows key
  case object App   extends KeyCode // Applications key
  case object Sleep extends KeyCode // Sleep key

  case object Numpad0         extends KeyCode // Numeric keypad 0
  case object Numpad1         extends KeyCode // Numeric keypad 1
  case object Numpad2         extends KeyCode // Numeric keypad 2
  case object Numpad3         extends KeyCode // Numeric keypad 3
  case object Numpad4         extends KeyCode // Numeric keypad 4
  case object Numpad5         extends KeyCode // Numeric keypad 5
  case object Numpad6         extends KeyCode // Numeric keypad 6
  case object Numpad7         extends KeyCode // Numeric keypad 7
  case object Numpad8         extends KeyCode // Numeric keypad 8
  case object Numpad9         extends KeyCode // Numeric keypad 9
  case object NumpadMultiply  extends KeyCode // Multiply
  case object NumpadAdd       extends KeyCode // Add
  case object NumpadSeparator extends KeyCode // Separator
  case object NumpadSubtract  extends KeyCode // Subtract
  case object NumpadDecimal   extends KeyCode // Decimal
  case object NumpadDivide    extends KeyCode // Divide

  case object F1  extends KeyCode // F1
  case object F2  extends KeyCode // F2
  case object F3  extends KeyCode // F3
  case object F4  extends KeyCode // F4
  case object F5  extends KeyCode // F5
  case object F6  extends KeyCode // F6
  case object F7  extends KeyCode // F7
  case object F8  extends KeyCode // F8
  case object F9  extends KeyCode // F9
  case object F10 extends KeyCode // F10
  case object F11 extends KeyCode // F11
  case object F12 extends KeyCode // F12

}
