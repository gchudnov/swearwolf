package com.github.gchudnov.swearwolf.util.colors

import com.github.gchudnov.swearwolf.util.show.Show
import com.github.gchudnov.swearwolf.util.func.MonadError

trait Color:
  def r: Byte
  def g: Byte
  def b: Byte

  def toHex(): String =
    "#" + List(r, g, b).map(Color.byteToHex).mkString


trait AnyColor[F[_]: MonadError, C <: Color]:

  def make(r: Byte, g: Byte, b: Byte): C

  def make(r: Int, g: Int, b: Int): C =
    make(r.toByte, g.toByte, b.toByte)

  def make(rgb: Int): C =
    make((rgb >> 16 & 0xff).toByte, (rgb >> 8 & 0xff).toByte, (rgb & 0xff).toByte)

  /**
   * Parses color from a string
   *
   * {{{
   *   #RRGGBB
   *    RRGGBB
   * }}}
   */
  def parse(value: String): F[C] =
    import MonadError.*
    if value.isEmpty then summon[MonadError[F]].fail(new ColorException("Cannot parse the color: <empty>"))
    else fromName(value).handleErrorWith(_ => fromHex(value))

  private def fromName(name: String): F[C] =
    val normalizedName = name.toLowerCase.replaceAll("_", "-")
    colors.get(normalizedName).fold(summon[MonadError[F]].fail(new ColorException(s"Named Color is not found: $name")): F[C])(c => summon[MonadError[F]].succeed(c))

  private def fromHex(value: String): F[C] =
    import MonadError.*

    val c = if value.head == '#' then value.tail else value
    if c.length != 6 then summon[MonadError[F]].fail(new ColorException("Cannot parse the color: invalid format. Supported formats: (#RRGGBB, RRGGBB)"))
    else
      for {
        cs <- summon[MonadError[F]].attempt(c.grouped(2).map(it => Integer.parseInt(it, 16).toByte).toSeq)
      } yield make(cs(0), cs(1), cs(2))

  val AliceBlue: C            = make(240, 248, 255)
  val AntiqueWhite: C         = make(250, 235, 215)
  val Aqua: C                 = make(0, 255, 255)
  val Aquamarine: C           = make(127, 255, 212)
  val Azure: C                = make(240, 255, 255)
  val Beige: C                = make(245, 245, 220)
  val Bisque: C               = make(255, 228, 196)
  val Black: C                = make(0, 0, 0)
  val BlanchedAlmond: C       = make(255, 235, 205)
  val Blue: C                 = make(0, 0, 255)
  val BlueViolet: C           = make(138, 43, 226)
  val Brown: C                = make(165, 42, 42)
  val BurlyWood: C            = make(222, 184, 135)
  val CadetBlue: C            = make(95, 158, 160)
  val Chartreuse: C           = make(127, 255, 0)
  val Chocolate: C            = make(210, 105, 30)
  val Coral: C                = make(255, 127, 80)
  val CornflowerBlue: C       = make(100, 149, 237)
  val Cornsilk: C             = make(255, 248, 220)
  val Crimson: C              = make(220, 20, 60)
  val Cyan: C                 = make(0, 255, 255)
  val DarkBlue: C             = make(0, 0, 139)
  val DarkCyan: C             = make(0, 139, 139)
  val DarkGoldenrod: C        = make(184, 134, 11)
  val DarkGray: C             = make(169, 169, 169)
  val DarkGreen: C            = make(0, 100, 0)
  val DarkKhaki: C            = make(189, 183, 107)
  val DarkMagenta: C          = make(139, 0, 139)
  val DarkOliveGreen: C       = make(85, 107, 47)
  val DarkOrange: C           = make(255, 140, 0)
  val DarkOrchid: C           = make(153, 50, 204)
  val DarkRed: C              = make(139, 0, 0)
  val DarkSalmon: C           = make(233, 150, 122)
  val DarkSeaGreen: C         = make(143, 188, 143)
  val DarkSlateBlue: C        = make(72, 61, 139)
  val DarkSlateGray: C        = make(47, 79, 79)
  val DarkTurquoise: C        = make(0, 206, 209)
  val DarkViolet: C           = make(148, 0, 211)
  val DeepPink: C             = make(255, 20, 147)
  val DeepSkyBlue: C          = make(0, 191, 255)
  val DimGray: C              = make(105, 105, 105)
  val DodgerBlue: C           = make(30, 144, 255)
  val FireBrick: C            = make(178, 34, 34)
  val FloralWhite: C          = make(255, 250, 240)
  val ForestGreen: C          = make(34, 139, 34)
  val Fuchsia: C              = make(255, 0, 255)
  val Gainsboro: C            = make(220, 220, 220)
  val GhostWhite: C           = make(248, 248, 255)
  val Gold: C                 = make(255, 215, 0)
  val Goldenrod: C            = make(218, 165, 32)
  val Gray: C                 = make(128, 128, 128)
  val Green: C                = make(0, 128, 0)
  val GreenYellow: C          = make(173, 255, 47)
  val Honeydew: C             = make(240, 255, 240)
  val HotPink: C              = make(255, 105, 180)
  val IndianRed: C            = make(205, 92, 92)
  val Indigo: C               = make(75, 0, 130)
  val Ivory: C                = make(255, 255, 240)
  val Khaki: C                = make(240, 230, 140)
  val Lavender: C             = make(230, 230, 250)
  val LavenderBlush: C        = make(255, 240, 245)
  val LawnGreen: C            = make(124, 252, 0)
  val LemonChiffon: C         = make(255, 250, 205)
  val LightBlue: C            = make(173, 216, 230)
  val LightCoral: C           = make(240, 128, 128)
  val LightCyan: C            = make(224, 255, 255)
  val LightGoldenrodYellow: C = make(250, 250, 210)
  val LightGray: C            = make(211, 211, 211)
  val LightGreen: C           = make(144, 238, 144)
  val LightPink: C            = make(255, 182, 193)
  val LightSalmon: C          = make(255, 160, 122)
  val LightSeaGreen: C        = make(32, 178, 170)
  val LightSkyBlue: C         = make(135, 206, 250)
  val LightSlateGray: C       = make(119, 136, 153)
  val LightSteelBlue: C       = make(176, 196, 222)
  val LightYellow: C          = make(255, 255, 224)
  val Lime: C                 = make(0, 255, 0)
  val LimeGreen: C            = make(50, 205, 50)
  val Linen: C                = make(250, 240, 230)
  val Magenta: C              = make(255, 0, 255)
  val Maroon: C               = make(128, 0, 0)
  val MediumAquamarine: C     = make(102, 205, 170)
  val MediumBlue: C           = make(0, 0, 205)
  val MediumOrchid: C         = make(186, 85, 211)
  val MediumPurple: C         = make(147, 112, 219)
  val MediumSeaGreen: C       = make(60, 179, 113)
  val MediumSlateBlue: C      = make(123, 104, 238)
  val MediumSpringGreen: C    = make(0, 250, 154)
  val MediumTurquoise: C      = make(72, 209, 204)
  val MediumVioletRed: C      = make(199, 21, 133)
  val MidnightBlue: C         = make(25, 25, 112)
  val MintCream: C            = make(245, 255, 250)
  val MistyRose: C            = make(255, 228, 225)
  val Moccasin: C             = make(255, 228, 181)
  val NavajoWhite: C          = make(255, 222, 173)
  val Navy: C                 = make(0, 0, 128)
  val OldLace: C              = make(253, 245, 230)
  val Olive: C                = make(128, 128, 0)
  val OliveDrab: C            = make(107, 142, 35)
  val Orange: C               = make(255, 165, 0)
  val OrangeRed: C            = make(255, 69, 0)
  val Orchid: C               = make(218, 112, 214)
  val PaleGoldenrod: C        = make(238, 232, 170)
  val PaleGreen: C            = make(152, 251, 152)
  val PaleTurquoise: C        = make(175, 238, 238)
  val PaleVioletRed: C        = make(219, 112, 147)
  val PapayaWhip: C           = make(255, 239, 213)
  val PeachPuff: C            = make(255, 218, 185)
  val Peru: C                 = make(205, 133, 63)
  val Pink: C                 = make(255, 192, 203)
  val Plum: C                 = make(221, 160, 221)
  val PowderBlue: C           = make(176, 224, 230)
  val Purple: C               = make(128, 0, 128)
  val Red: C                  = make(255, 0, 0)
  val RosyBrown: C            = make(188, 143, 143)
  val RoyalBlue: C            = make(65, 105, 225)
  val SaddleBrown: C          = make(139, 69, 19)
  val Salmon: C               = make(250, 128, 114)
  val SandyBrown: C           = make(244, 164, 96)
  val SeaGreen: C             = make(46, 139, 87)
  val Seashell: C             = make(255, 245, 238)
  val Sienna: C               = make(160, 82, 45)
  val Silver: C               = make(192, 192, 192)
  val SkyBlue: C              = make(135, 206, 235)
  val SlateBlue: C            = make(106, 90, 205)
  val SlateGray: C            = make(112, 128, 144)
  val Snow: C                 = make(255, 250, 250)
  val SpringGreen: C          = make(0, 255, 127)
  val SteelBlue: C            = make(70, 130, 180)
  val Tan: C                  = make(210, 180, 140)
  val Teal: C                 = make(0, 128, 128)
  val Thistle: C              = make(216, 191, 216)
  val Tomato: C               = make(255, 99, 71)
  val Turquoise: C            = make(64, 224, 208)
  val Violet: C               = make(238, 130, 238)
  val Wheat: C                = make(245, 222, 179)
  val White: C                = make(255, 255, 255)
  val WhiteSmoke: C           = make(245, 245, 245)
  val Yellow: C               = make(255, 255, 0)
  val YellowGreen: C          = make(154, 205, 50)

  private lazy val colors: Map[String, C] = Map(
    Names.AliceBlue            -> AliceBlue,
    Names.AntiqueWhite         -> AntiqueWhite,
    Names.Aqua                 -> Aqua,
    Names.Aquamarine           -> Aquamarine,
    Names.Azure                -> Azure,
    Names.Beige                -> Beige,
    Names.Bisque               -> Bisque,
    Names.Black                -> Black,
    Names.BlanchedAlmond       -> BlanchedAlmond,
    Names.Blue                 -> Blue,
    Names.BlueViolet           -> BlueViolet,
    Names.Brown                -> Brown,
    Names.BurlyWood            -> BurlyWood,
    Names.CadetBlue            -> CadetBlue,
    Names.Chartreuse           -> Chartreuse,
    Names.Chocolate            -> Chocolate,
    Names.Coral                -> Coral,
    Names.CornflowerBlue       -> CornflowerBlue,
    Names.Cornsilk             -> Cornsilk,
    Names.Crimson              -> Crimson,
    Names.Cyan                 -> Cyan,
    Names.DarkBlue             -> DarkBlue,
    Names.DarkCyan             -> DarkCyan,
    Names.DarkGldenrod         -> DarkGoldenrod,
    Names.DarkGray             -> DarkGray,
    Names.DarkGreen            -> DarkGreen,
    Names.DarkKhaki            -> DarkKhaki,
    Names.DarkMagenta          -> DarkMagenta,
    Names.DarkOliveGreen       -> DarkOliveGreen,
    Names.DarkOrange           -> DarkOrange,
    Names.DarkOrchid           -> DarkOrchid,
    Names.DarkRed              -> DarkRed,
    Names.DarkSalmon           -> DarkSalmon,
    Names.DarkSeaGreen         -> DarkSeaGreen,
    Names.DarkSlateBlue        -> DarkSlateBlue,
    Names.DarkSlateGray        -> DarkSlateGray,
    Names.DarkTurquoise        -> DarkTurquoise,
    Names.DarkViolet           -> DarkViolet,
    Names.DeepPink             -> DeepPink,
    Names.DeepSkyBlue          -> DeepSkyBlue,
    Names.DimGray              -> DimGray,
    Names.DodgerBlue           -> DodgerBlue,
    Names.FireBrick            -> FireBrick,
    Names.FloralWhite          -> FloralWhite,
    Names.ForestGreen          -> ForestGreen,
    Names.Fuchsia              -> Fuchsia,
    Names.Gainsboro            -> Gainsboro,
    Names.GhostWhite           -> GhostWhite,
    Names.Gold                 -> Gold,
    Names.Goldenrod            -> Goldenrod,
    Names.Gray                 -> Gray,
    Names.Green                -> Green,
    Names.GreenYellow          -> GreenYellow,
    Names.Honeydew             -> Honeydew,
    Names.HotPink              -> HotPink,
    Names.IndianRed            -> IndianRed,
    Names.Indigo               -> Indigo,
    Names.Ivory                -> Ivory,
    Names.Khaki                -> Khaki,
    Names.Lavender             -> Lavender,
    Names.LavenderBlush        -> LavenderBlush,
    Names.LawnGreen            -> LawnGreen,
    Names.LemonChiffon         -> LemonChiffon,
    Names.LightBlue            -> LightBlue,
    Names.LightCoral           -> LightCoral,
    Names.LightCyan            -> LightCyan,
    Names.LightGoldenrodYellow -> LightGoldenrodYellow,
    Names.LightGray            -> LightGray,
    Names.LightGreen           -> LightGreen,
    Names.LightPink            -> LightPink,
    Names.LightSalmon          -> LightSalmon,
    Names.LightSeaGreen        -> LightSeaGreen,
    Names.LightSkyBlue         -> LightSkyBlue,
    Names.LightSlateGray       -> LightSlateGray,
    Names.LightSteelBlue       -> LightSteelBlue,
    Names.LightYellow          -> LightYellow,
    Names.Lime                 -> Lime,
    Names.LimeGreen            -> LimeGreen,
    Names.Linen                -> Linen,
    Names.Magenta              -> Magenta,
    Names.Maroon               -> Maroon,
    Names.MediumAquamarine     -> MediumAquamarine,
    Names.MediumBlue           -> MediumBlue,
    Names.MediumOrchid         -> MediumOrchid,
    Names.MediumPurple         -> MediumPurple,
    Names.MediumSeaGreen       -> MediumSeaGreen,
    Names.MediumSlateBlue      -> MediumSlateBlue,
    Names.MediumSpringGreen    -> MediumSpringGreen,
    Names.MediumTurquoise      -> MediumTurquoise,
    Names.MediumVioletRed      -> MediumVioletRed,
    Names.MidnightBlue         -> MidnightBlue,
    Names.MintCream            -> MintCream,
    Names.MistyRose            -> MistyRose,
    Names.Moccasin             -> Moccasin,
    Names.NavajoWhite          -> NavajoWhite,
    Names.Navy                 -> Navy,
    Names.OldLace              -> OldLace,
    Names.Olive                -> Olive,
    Names.OliveDrab            -> OliveDrab,
    Names.Orange               -> Orange,
    Names.OrangeRed            -> OrangeRed,
    Names.Orchid               -> Orchid,
    Names.PaleGoldenrod        -> PaleGoldenrod,
    Names.PaleGreen            -> PaleGreen,
    Names.PaleTurquoise        -> PaleTurquoise,
    Names.PaleVioletRed        -> PaleVioletRed,
    Names.PapayaWhip           -> PapayaWhip,
    Names.PeachPuff            -> PeachPuff,
    Names.Peru                 -> Peru,
    Names.Pink                 -> Pink,
    Names.Plum                 -> Plum,
    Names.PowderBlue           -> PowderBlue,
    Names.Purple               -> Purple,
    Names.Red                  -> Red,
    Names.RosyBrown            -> RosyBrown,
    Names.RoyalBlue            -> RoyalBlue,
    Names.SaddleBrown          -> SaddleBrown,
    Names.Salmon               -> Salmon,
    Names.SandyBrown           -> SandyBrown,
    Names.SeaGreen             -> SeaGreen,
    Names.Seashell             -> Seashell,
    Names.Sienna               -> Sienna,
    Names.Silver               -> Silver,
    Names.SkyBlue              -> SkyBlue,
    Names.SlateBlue            -> SlateBlue,
    Names.SlateGray            -> SlateGray,
    Names.Snow                 -> Snow,
    Names.SpringGreen          -> SpringGreen,
    Names.SteelBlue            -> SteelBlue,
    Names.Tan                  -> Tan,
    Names.Teal                 -> Teal,
    Names.Thistle              -> Thistle,
    Names.Tomato               -> Tomato,
    Names.Turquoise            -> Turquoise,
    Names.Violet               -> Violet,
    Names.Wheat                -> Wheat,
    Names.White                -> White,
    Names.WhiteSmoke           -> WhiteSmoke,
    Names.Yellow               -> Yellow,
    Names.YellowGreen          -> YellowGreen
  )


object Color:

  private def byteToHex(n: Byte): String =
    f"${n}%02x"

  given Show[Color] with
    extension (a: Color)
      def show: String =
        a.toHex()
