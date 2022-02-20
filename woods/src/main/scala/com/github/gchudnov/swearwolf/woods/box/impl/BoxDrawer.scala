package com.github.gchudnov.swearwolf.woods.box.impl

import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.util.{ Point }
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.woods.{ Box, BoxStyle }
import com.github.gchudnov.swearwolf.woods.util.Symbols
import com.github.gchudnov.swearwolf.woods.util.impl.Func
import com.github.gchudnov.swearwolf.woods.BoxStyle.*

private[box] object BoxDrawer:

  def draw(screen: Screen)(pt: Point, box: Box, textStyle: TextStyle): Either[Throwable, Unit] =
    if box.size.width < 2 || box.size.height < 2 then Right(()) // Box is too small to be displayed
    else {
      val BoxDesc(ht, hb, vl, vr, tl, tr, bl, br) = getDesc(box.style)

      val horzTopLine    = Range(0, box.size.width - 2).map(_ => ht).mkString
      val horzBottomLine = Range(0, box.size.width - 2).map(_ => hb).mkString
      val topLine        = tl ++ horzTopLine ++ tr
      val bottomLine     = bl ++ horzBottomLine ++ br

      for
        _ <- screen.put(pt, topLine, textStyle)
        _ <- screen.put(pt.offset(0, box.size.height - 1), bottomLine, textStyle)
        _ <- Func.sequence(Range(0, box.size.height - 2).map { y =>
               for
                 _ <- screen.put(pt.offset(0, y + 1), vl, textStyle)
                 _ <- screen.put(pt.offset(box.size.width - 1, y + 1), vr, textStyle)
               yield ()
             })
      yield ()
    }

  private def getDesc(style: BoxStyle): BoxDesc =
    style match
      case Empty =>
        BoxDesc(
          ht = Symbols.Empty,
          hb = Symbols.Empty,
          vl = Symbols.Empty,
          vr = Symbols.Empty,
          tl = Symbols.Empty,
          tr = Symbols.Empty,
          bl = Symbols.Empty,
          br = Symbols.Empty
        )

      case SingleBorder =>
        BoxDesc(
          ht = Symbols.Frame_DEF,
          hb = Symbols.Frame_DEF,
          vl = Symbols.Frame_BEH,
          vr = Symbols.Frame_BEH,
          tl = Symbols.Frame_EFH,
          tr = Symbols.Frame_DEH,
          bl = Symbols.Frame_BEF,
          br = Symbols.Frame_BED
        )
      case DoubleBorder =>
        BoxDesc(
          ht = Symbols.Frame_PQR,
          hb = Symbols.Frame_PQR,
          vl = Symbols.Frame_NQT,
          vr = Symbols.Frame_NQT,
          tl = Symbols.Frame_TQR,
          tr = Symbols.Frame_QRU,
          bl = Symbols.Frame_NQR,
          br = Symbols.Frame_NQP
        )

      case BoldBorder =>
        BoxDesc(
          ht = Symbols.BlockUpper_4_8,
          hb = Symbols.BlockLower_4_8,
          vl = Symbols.BlockLeft_4_8,
          vr = Symbols.BlockRight_4_8,
          tl = Symbols.Quadrant_ABC,
          tr = Symbols.Quadrant_ABD,
          bl = Symbols.Quadrant_ACD,
          br = Symbols.Quadrant_BCD
        )

  private final case class BoxDesc(
    ht: String, // horizontal-top
    hb: String, // horizontal-bottom
    vl: String, // vertical-left
    vr: String, // vertical-right
    tl: String, // top-left
    tr: String, // top-right
    bl: String, // bottom-left
    br: String  // bottom-right
  )
