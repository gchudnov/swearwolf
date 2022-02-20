package com.github.gchudnov.swearwolf.util.colors

final case class Color(r: Int, g: Int, b: Int)

object Color:

  /**
   * Parses color from a string
   *
   * {{{
   *   #RRGGBB
   *    RRGGBB
   * }}}
   */
  def parse(value: String): Either[Throwable, Color] =
    if value.isEmpty then Left(new ColorException("Cannot parse the color: <empty>"))
    else fromName(value).orElse(fromHex(value))

  /**
   * converts color to a hex value
   * @param color
   *   Color to covert
   * @return
   *   hex value of the color with leading #
   */
  def toHex(color: Color): String =
    s"#${f"${color.r}%02x"}${f"${color.g}%02x"}${f"${color.b}%02x"}"

  private def fromName(name: String): Either[Throwable, Color] =
    require(name.nonEmpty, "Color Name must be non-empty")
    NamedColor
      .parse(name)

  private def fromHex(value: String): Either[Throwable, Color] =
    require(value.nonEmpty, "Color Value must be non-empty")
    val c = if value.head == '#' then value.tail else value
    if c.length != 6 then Left(new ColorException("Cannot parse the color: invalid format. Supported formats: (#RRGGBB, RRGGBB)"))
    else {
      val cs = c.grouped(2).map(it => Integer.parseInt(it, 16)).toSeq
      Right(Color(cs(0), cs(1), cs(2)))
    }
