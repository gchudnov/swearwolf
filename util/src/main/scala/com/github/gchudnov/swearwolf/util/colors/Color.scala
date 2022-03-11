package com.github.gchudnov.swearwolf.util.colors

import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.show.Show

final case class Color(r: Byte, g: Byte, b: Byte):
  def toHex(): String =
    "#" + List(r, g, b).map(Color.byteToHex).mkString

abstract class AnyColor[F[_]](using ME: MonadError[F]):

  /**
   * Parses color from a string
   *
   * {{{
   *   #RRGGBB
   *    RRGGBB
   * }}}
   */
  def parse(value: String): F[Color] =
    import MonadError.*
    if value.isEmpty then summon[MonadError[F]].fail(new ColorException("Cannot parse the color: <empty>"))
    else fromName(value).handleErrorWith(_ => fromHex(value))

  private def fromName(name: String): F[Color] =
    Color.colors.get(normalizeName(name)).fold(summon[MonadError[F]].fail(new ColorException(s"Named Color is not found: $name")): F[Color])(c => summon[MonadError[F]].succeed(c))

  private def fromHex(value: String): F[Color] =
    import MonadError.*

    val c = if value.head == '#' then value.tail else value
    if c.length != 6 then summon[MonadError[F]].fail(new ColorException("Cannot parse the color: invalid format. Supported formats: (#RRGGBB, RRGGBB)"))
    else
      for cs <- summon[MonadError[F]].attempt(c.grouped(2).map(it => Integer.parseInt(it, 16).toByte).toSeq)
      yield Color(cs(0), cs(1), cs(2))

  private def normalizeName(name: String): String =
    name.toLowerCase.replaceAll("_", "-")

object Color:

  def apply(r: Byte, g: Byte, b: Byte): Color = new Color(r, g, b)

  def apply(r: Int, g: Int, b: Int): Color =
    apply(r.toByte, g.toByte, b.toByte)

  def apply(rgb: Int): Color =
    apply((rgb >> 16 & 0xff).toByte, (rgb >> 8 & 0xff).toByte, (rgb & 0xff).toByte)

  private def byteToHex(n: Byte): String =
    f"${n}%02x"

  given Show[Color] with
    extension (a: Color)
      def show: String =
        a.toHex()

  val AliceBlue: Color            = Color(240, 248, 255)
  val AntiqueWhite: Color         = Color(250, 235, 215)
  val Aqua: Color                 = Color(0, 255, 255)
  val Aquamarine: Color           = Color(127, 255, 212)
  val Azure: Color                = Color(240, 255, 255)
  val Beige: Color                = Color(245, 245, 220)
  val Bisque: Color               = Color(255, 228, 196)
  val Black: Color                = Color(0, 0, 0)
  val BlanchedAlmond: Color       = Color(255, 235, 205)
  val Blue: Color                 = Color(0, 0, 255)
  val BlueViolet: Color           = Color(138, 43, 226)
  val Brown: Color                = Color(165, 42, 42)
  val BurlyWood: Color            = Color(222, 184, 135)
  val CadetBlue: Color            = Color(95, 158, 160)
  val Chartreuse: Color           = Color(127, 255, 0)
  val Chocolate: Color            = Color(210, 105, 30)
  val Coral: Color                = Color(255, 127, 80)
  val CornflowerBlue: Color       = Color(100, 149, 237)
  val Cornsilk: Color             = Color(255, 248, 220)
  val Crimson: Color              = Color(220, 20, 60)
  val Cyan: Color                 = Color(0, 255, 255)
  val DarkBlue: Color             = Color(0, 0, 139)
  val DarkCyan: Color             = Color(0, 139, 139)
  val DarkGoldenrod: Color        = Color(184, 134, 11)
  val DarkGray: Color             = Color(169, 169, 169)
  val DarkGreen: Color            = Color(0, 100, 0)
  val DarkKhaki: Color            = Color(189, 183, 107)
  val DarkMagenta: Color          = Color(139, 0, 139)
  val DarkOliveGreen: Color       = Color(85, 107, 47)
  val DarkOrange: Color           = Color(255, 140, 0)
  val DarkOrchid: Color           = Color(153, 50, 204)
  val DarkRed: Color              = Color(139, 0, 0)
  val DarkSalmon: Color           = Color(233, 150, 122)
  val DarkSeaGreen: Color         = Color(143, 188, 143)
  val DarkSlateBlue: Color        = Color(72, 61, 139)
  val DarkSlateGray: Color        = Color(47, 79, 79)
  val DarkTurquoise: Color        = Color(0, 206, 209)
  val DarkViolet: Color           = Color(148, 0, 211)
  val DeepPink: Color             = Color(255, 20, 147)
  val DeepSkyBlue: Color          = Color(0, 191, 255)
  val DimGray: Color              = Color(105, 105, 105)
  val DodgerBlue: Color           = Color(30, 144, 255)
  val FireBrick: Color            = Color(178, 34, 34)
  val FloralWhite: Color          = Color(255, 250, 240)
  val ForestGreen: Color          = Color(34, 139, 34)
  val Fuchsia: Color              = Color(255, 0, 255)
  val Gainsboro: Color            = Color(220, 220, 220)
  val GhostWhite: Color           = Color(248, 248, 255)
  val Gold: Color                 = Color(255, 215, 0)
  val Goldenrod: Color            = Color(218, 165, 32)
  val Gray: Color                 = Color(128, 128, 128)
  val Green: Color                = Color(0, 128, 0)
  val GreenYellow: Color          = Color(173, 255, 47)
  val Honeydew: Color             = Color(240, 255, 240)
  val HotPink: Color              = Color(255, 105, 180)
  val IndianRed: Color            = Color(205, 92, 92)
  val Indigo: Color               = Color(75, 0, 130)
  val Ivory: Color                = Color(255, 255, 240)
  val Khaki: Color                = Color(240, 230, 140)
  val Lavender: Color             = Color(230, 230, 250)
  val LavenderBlush: Color        = Color(255, 240, 245)
  val LawnGreen: Color            = Color(124, 252, 0)
  val LemonChiffon: Color         = Color(255, 250, 205)
  val LightBlue: Color            = Color(173, 216, 230)
  val LightCoral: Color           = Color(240, 128, 128)
  val LightCyan: Color            = Color(224, 255, 255)
  val LightGoldenrodYellow: Color = Color(250, 250, 210)
  val LightGray: Color            = Color(211, 211, 211)
  val LightGreen: Color           = Color(144, 238, 144)
  val LightPink: Color            = Color(255, 182, 193)
  val LightSalmon: Color          = Color(255, 160, 122)
  val LightSeaGreen: Color        = Color(32, 178, 170)
  val LightSkyBlue: Color         = Color(135, 206, 250)
  val LightSlateGray: Color       = Color(119, 136, 153)
  val LightSteelBlue: Color       = Color(176, 196, 222)
  val LightYellow: Color          = Color(255, 255, 224)
  val Lime: Color                 = Color(0, 255, 0)
  val LimeGreen: Color            = Color(50, 205, 50)
  val Linen: Color                = Color(250, 240, 230)
  val Magenta: Color              = Color(255, 0, 255)
  val Maroon: Color               = Color(128, 0, 0)
  val MediumAquamarine: Color     = Color(102, 205, 170)
  val MediumBlue: Color           = Color(0, 0, 205)
  val MediumOrchid: Color         = Color(186, 85, 211)
  val MediumPurple: Color         = Color(147, 112, 219)
  val MediumSeaGreen: Color       = Color(60, 179, 113)
  val MediumSlateBlue: Color      = Color(123, 104, 238)
  val MediumSpringGreen: Color    = Color(0, 250, 154)
  val MediumTurquoise: Color      = Color(72, 209, 204)
  val MediumVioletRed: Color      = Color(199, 21, 133)
  val MidnightBlue: Color         = Color(25, 25, 112)
  val MintCream: Color            = Color(245, 255, 250)
  val MistyRose: Color            = Color(255, 228, 225)
  val Moccasin: Color             = Color(255, 228, 181)
  val NavajoWhite: Color          = Color(255, 222, 173)
  val Navy: Color                 = Color(0, 0, 128)
  val OldLace: Color              = Color(253, 245, 230)
  val Olive: Color                = Color(128, 128, 0)
  val OliveDrab: Color            = Color(107, 142, 35)
  val Orange: Color               = Color(255, 165, 0)
  val OrangeRed: Color            = Color(255, 69, 0)
  val Orchid: Color               = Color(218, 112, 214)
  val PaleGoldenrod: Color        = Color(238, 232, 170)
  val PaleGreen: Color            = Color(152, 251, 152)
  val PaleTurquoise: Color        = Color(175, 238, 238)
  val PaleVioletRed: Color        = Color(219, 112, 147)
  val PapayaWhip: Color           = Color(255, 239, 213)
  val PeachPuff: Color            = Color(255, 218, 185)
  val Peru: Color                 = Color(205, 133, 63)
  val Pink: Color                 = Color(255, 192, 203)
  val Plum: Color                 = Color(221, 160, 221)
  val PowderBlue: Color           = Color(176, 224, 230)
  val Purple: Color               = Color(128, 0, 128)
  val Red: Color                  = Color(255, 0, 0)
  val RosyBrown: Color            = Color(188, 143, 143)
  val RoyalBlue: Color            = Color(65, 105, 225)
  val SaddleBrown: Color          = Color(139, 69, 19)
  val Salmon: Color               = Color(250, 128, 114)
  val SandyBrown: Color           = Color(244, 164, 96)
  val SeaGreen: Color             = Color(46, 139, 87)
  val Seashell: Color             = Color(255, 245, 238)
  val Sienna: Color               = Color(160, 82, 45)
  val Silver: Color               = Color(192, 192, 192)
  val SkyBlue: Color              = Color(135, 206, 235)
  val SlateBlue: Color            = Color(106, 90, 205)
  val SlateGray: Color            = Color(112, 128, 144)
  val Snow: Color                 = Color(255, 250, 250)
  val SpringGreen: Color          = Color(0, 255, 127)
  val SteelBlue: Color            = Color(70, 130, 180)
  val Tan: Color                  = Color(210, 180, 140)
  val Teal: Color                 = Color(0, 128, 128)
  val Thistle: Color              = Color(216, 191, 216)
  val Tomato: Color               = Color(255, 99, 71)
  val Turquoise: Color            = Color(64, 224, 208)
  val Violet: Color               = Color(238, 130, 238)
  val Wheat: Color                = Color(245, 222, 179)
  val White: Color                = Color(255, 255, 255)
  val WhiteSmoke: Color           = Color(245, 245, 245)
  val Yellow: Color               = Color(255, 255, 0)
  val YellowGreen: Color          = Color(154, 205, 50)

  lazy val colors: Map[String, Color] = Map(
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
