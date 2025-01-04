package com.github.gchudnov.swearwolf.shapes.styles

/**
 * https://www.utf8-chartable.de/unicode-utf8-table.pl?start=9472&unicodeinhtml=dec
 */
object Symbols:

  val NotFound = "?"
  val Empty    = " "

  val CharPlus          = "+"
  val CharMinus         = "-"
  val CharEqual         = "="
  val CharVertSeparator = "|"

  val BlockLower_1_8       = "▁" // U+2581 ; Lower 1/8
  val BlockLower_2_8       = "▂" // U+2582 ; Lower 2/8
  val BlockLower_3_8       = "▃" // U+2583 ; Lower 3/8
  val BlockLower_4_8       = "▄" // U+2584 ; Lower 4/8
  val BlockLower_5_8       = "▅" // U+2585 ; Lower 5/8
  val BlockLower_6_8       = "▆" // U+2586 ; Lower 6/8
  val BlockLower_7_8       = "▇" // U+2587 ; Lower 7/8
  val BlockFull_8_8        = "█" // U+2588 ; Full 8/8
  val BlockLeft_7_8        = "▉" // U+2589 ; Left 7/8
  val BlockLeft_6_8        = "▊" // U+258A ; Left 6/8
  val BlockLeft_5_8        = "▋" // U+258B ; Left 5/8
  val BlockLeft_4_8        = "▌" // U+258C ; Left 4/8
  val BlockLeft_3_8        = "▍" // U+258D ; Left 3/8
  val BlockLeft_2_8        = "▎" // U+258E ; Left 2/8
  val BlockLeft_1_8        = "▏" // U+258F ; Left 1/8
  val BlockRight_1_8       = "▕" // U+2595 ; Right 1/8
  val BlockRight_4_8       = "▐" // U+2590 ; Right 4/8
  val BlockUpper_1_8       = "▔" // U+2594 ; Upper 1/8
  val BlockUpper_4_8       = "▀" // U+2580 ; Upper 4/8
  val BlockLightShade_8_8  = "░" // U+2591
  val BlockMediumShade_8_8 = "▒" // U+2592
  val BlockDarkShade_8_8   = "▓" // U+2593

  // Quadrant
  // |A B|
  // |C D|
  // ACD means: "▙"
  val Quadrant_C    = "▖" // U+2596
  val Quadrant_D    = "▗" // U+2597
  val Quadrant_A    = "▘" // U+2598
  val Quadrant_B    = "▝" // U+259D
  val Quadrant_ACD  = "▙" // U+2599
  val Quadrant_AD   = "▚" // U+259A
  val Quadrant_BC   = "▞" //  U+259E
  val Quadrant_ABC  = "▛" // U+259B
  val Quadrant_ABD  = "▜" // U+259C
  val Quadrant_BCD  = "▟" // U+259F
  val Quadrant_ABCD = BlockFull_8_8

  // single-line
  // A B C
  // D E F
  // G H I
  //
  // double-line
  // M N O
  // P Q R
  // S T U
  val Frame_BEH    = "│"
  val Frame_NQT    = "║"
  val Frame_BDEH   = "┤"
  val Frame_BPEH   = "╡"
  val Frame_DNQT   = "╢"
  val Frame_NPQT   = "╣"
  val Frame_BEFH   = "├"
  val Frame_BEHR   = "╞"
  val Frame_FNQT   = "╟"
  val Frame_NQRT   = "╠"
  val Frame_DEF    = "─"
  val Frame_PQR    = "═"
  val Frame_BDEF   = "┴"
  val Frame_DEFN   = "╨"
  val Frame_BPQR   = "╧"
  val Frame_NPQR   = "╩"
  val Frame_DEFH   = "┬"
  val Frame_DEFT   = "╥"
  val Frame_PQRH   = "╤"
  val Frame_PQRT   = "╦"
  val Frame_DEH    = "┐"
  val Frame_DET    = "╖"
  val Frame_QRI    = "╕"
  val Frame_QRU    = "╗"
  val Frame_BED    = "┘"
  val Frame_BEP    = "╛"
  val Frame_NQD    = "╜"
  val Frame_NQP    = "╝"
  val Frame_BEF    = "└"
  val Frame_NQF    = "╙"
  val Frame_BQR    = "╘"
  val Frame_NQR    = "╚"
  val Frame_EFH    = "┌"
  val Frame_HQR    = "╒"
  val Frame_TQF    = "╓"
  val Frame_TQR    = "╔"
  val Frame_BDEFH  = "┼"
  val Frame_DEFNQT = "╫"
  val Frame_BEHPQR = "╪"
  val Frame_NQTPQR = "╬"

  val BoxDash2Horz              = "╌"
  val BoxDash2HeavyHorz         = "╍"
  val BoxDash3Horz              = "┄"
  val BoxDash3HeavyHorz         = "┅"
  val BoxDash4Horz              = "┈"
  val BoxDash4HeavyHorz         = "┉"
  val BoxDash2Vert              = "╎"
  val BoxDash2HeavyVert         = "╏"
  val BoxDash3Vert              = "┆"
  val BoxDash3HeavyVert         = "┇"
  val BoxDash4Vert              = "┊"
  val BoxDash4HeavyVert         = "┋"
  val BoxLineHorz               = "─"
  val BoxLineHeavyHorz          = "━"
  val BoxLineVert               = "│"
  val BoxLineHeavyVert          = "┃"
  val BoxCornerTopLeft          = "┌"
  val BoxCornerHeavyTopLeft     = "┏"
  val BoxCornerTopRight         = "┐"
  val BoxCornerHeavyTopRight    = "┓"
  val BoxCornerBottomLeft       = "└"
  val BoxCornerHeavyBottomLeft  = "┗"
  val BoxCornerBottomRight      = "┘"
  val BoxCornerHeavyBottomRight = "┛"
  val BoxIntersectLeft          = "├"
  val BoxIntersectHeavyLeft     = "┣"
  val BoxIntersectRight         = "┤"
  val BoxIntersectHeavyRight    = "┫"
  val BoxIntersectTop           = "┬"
  val BoxIntersectHeavyTop      = "┳"
  val BoxIntersectBottom        = "┴"
  val BoxIntersectHeavyBottom   = "┻"
  val BoxIntersect              = "┼"
  val BoxIntersectHeavy         = "╋"

  val Subscript_0 = "₀"
  val Subscript_1 = "₁"
  val Subscript_2 = "₂"
  val Subscript_3 = "₃"
  val Subscript_4 = "₄"
  val Subscript_5 = "₅"
  val Subscript_6 = "₆"
  val Subscript_7 = "₇"
  val Subscript_8 = "₈"
  val Subscript_9 = "₉"

  val TriangleUp         = "▲" // U+25B2
  val TriangleWhiteUp    = "△" // U+25B3
  val TriangleDown       = "▼" // U+25BC
  val TriangleWhiteDown  = "▽" // U+25BD
  val TriangleRight      = "▶" // U+25B6
  val TriangleWhiteRight = "▷" // U+25B7
  val TriangleLeft       = "◀" // U+25C0
  val TriangleWhiteLeft  = "◁" // U+25C1

  val Degree               = "°"
  val Check                = "√"
  val RoundDot             = "●" // U+25CF
  val RoundWhiteDot        = "○" // U+25CB
  val RectDot              = "■" // U+25A0
  val RectWhiteDot         = "□" // U+25A1
  val RectWhiteDot2        = "▣" // U+25A3
  val RectFillHorzDot      = "▤" // U+25A4
  val RectFillVertDot      = "▥" // U+25A5
  val RectFillCrossDot     = "▦" // U+25A6
  val RectFillDiagLeftDot  = "▧" // U+25A7
  val RectFillDiagRightDot = "▨" // U+25A8
  val RectFillCrossDiagDot = "▩" // U+25A9

  val DotsUp_0_0 = " "
  val DotsUp_0_1 = "⢀"
  val DotsUp_0_2 = "⢠"
  val DotsUp_0_3 = "⢰"
  val DotsUp_0_4 = "⢸"
  val DotsUp_1_0 = "⡀"
  val DotsUp_1_1 = "⣀"
  val DotsUp_1_2 = "⣠"
  val DotsUp_1_3 = "⣰"
  val DotsUp_1_4 = "⣸"
  val DotsUp_2_0 = "⡄"
  val DotsUp_2_1 = "⣄"
  val DotsUp_2_2 = "⣤"
  val DotsUp_2_3 = "⣴"
  val DotsUp_2_4 = "⣼"
  val DotsUp_3_0 = "⡆"
  val DotsUp_3_1 = "⣆"
  val DotsUp_3_2 = "⣦"
  val DotsUp_3_3 = "⣶"
  val DotsUp_3_4 = "⣾"
  val DotsUp_4_0 = "⡇"
  val DotsUp_4_1 = "⣇"
  val DotsUp_4_2 = "⣧"
  val DotsUp_4_3 = "⣷"
  val DotsUp_4_4 = "⣿"

  val DotsDown_0_0 = " "
  val DotsDown_0_1 = "⠈"
  val DotsDown_0_2 = "⠘"
  val DotsDown_0_3 = "⠸"
  val DotsDown_0_4 = "⢸"
  val DotsDown_1_0 = "⠁"
  val DotsDown_1_1 = "⠉"
  val DotsDown_1_2 = "⠙"
  val DotsDown_1_3 = "⠹"
  val DotsDown_1_4 = "⢹"
  val DotsDown_2_0 = "⠃"
  val DotsDown_2_1 = "⠋"
  val DotsDown_2_2 = "⠛"
  val DotsDown_2_3 = "⠻"
  val DotsDown_2_4 = "⢻"
  val DotsDown_3_0 = "⠇"
  val DotsDown_3_1 = "⠏"
  val DotsDown_3_2 = "⠟"
  val DotsDown_3_3 = "⠿"
  val DotsDown_3_4 = "⢿"
  val DotsDown_4_0 = "⡇"
  val DotsDown_4_1 = "⡏"
  val DotsDown_4_2 = "⡟"
  val DotsDown_4_3 = "⡿"
  val DotsDown_4_4 = "⣿"

  val ArrowMax = "⭱"
  val ArrowMin = "⭳"

  val quadChartUp: Map[String, String] = Map(
    "0_0" -> Empty,
    "0_1" -> Quadrant_D,
    "0_2" -> BlockRight_4_8,
    "1_0" -> Quadrant_C,
    "1_1" -> BlockLower_4_8,
    "1_2" -> Quadrant_BCD,
    "2_0" -> BlockLeft_4_8,
    "2_1" -> Quadrant_ACD,
    "2_2" -> BlockFull_8_8,
  )

  val stepChartUp: Map[String, String] = Map(
    "0" -> Empty,
    "1" -> BlockLower_1_8,
    "2" -> BlockLower_2_8,
    "3" -> BlockLower_3_8,
    "4" -> BlockLower_4_8,
    "5" -> BlockLower_5_8,
    "6" -> BlockLower_6_8,
    "7" -> BlockLower_7_8,
    "8" -> BlockFull_8_8,
  )

  val dotChartUp: Map[String, String] = Map(
    "0_0" -> DotsUp_0_0,
    "0_1" -> DotsUp_0_1,
    "0_2" -> DotsUp_0_2,
    "0_3" -> DotsUp_0_3,
    "0_4" -> DotsUp_0_4,
    "1_0" -> DotsUp_1_0,
    "1_1" -> DotsUp_1_1,
    "1_2" -> DotsUp_1_2,
    "1_3" -> DotsUp_1_3,
    "1_4" -> DotsUp_1_4,
    "2_0" -> DotsUp_2_0,
    "2_1" -> DotsUp_2_1,
    "2_2" -> DotsUp_2_2,
    "2_3" -> DotsUp_2_3,
    "2_4" -> DotsUp_2_4,
    "3_0" -> DotsUp_3_0,
    "3_1" -> DotsUp_3_1,
    "3_2" -> DotsUp_3_2,
    "3_3" -> DotsUp_3_3,
    "3_4" -> DotsUp_3_4,
    "4_0" -> DotsUp_4_0,
    "4_1" -> DotsUp_4_1,
    "4_2" -> DotsUp_4_2,
    "4_3" -> DotsUp_4_3,
    "4_4" -> DotsUp_4_4,
  )

  val dotChartDown: Map[String, String] = Map(
    "0_0" -> DotsDown_0_0,
    "0_1" -> DotsDown_0_1,
    "0_2" -> DotsDown_0_2,
    "0_3" -> DotsDown_0_3,
    "0_4" -> DotsDown_0_4,
    "1_0" -> DotsDown_1_0,
    "1_1" -> DotsDown_1_1,
    "1_2" -> DotsDown_1_2,
    "1_3" -> DotsDown_1_3,
    "1_4" -> DotsDown_1_4,
    "2_0" -> DotsDown_2_0,
    "2_1" -> DotsDown_2_1,
    "2_2" -> DotsDown_2_2,
    "2_3" -> DotsDown_2_3,
    "2_4" -> DotsDown_2_4,
    "3_0" -> DotsDown_3_0,
    "3_1" -> DotsDown_3_1,
    "3_2" -> DotsDown_3_2,
    "3_3" -> DotsDown_3_3,
    "3_4" -> DotsDown_3_4,
    "4_0" -> DotsDown_4_0,
    "4_1" -> DotsDown_4_1,
    "4_2" -> DotsDown_4_2,
    "4_3" -> DotsDown_4_3,
    "4_4" -> DotsDown_4_4,
  )
