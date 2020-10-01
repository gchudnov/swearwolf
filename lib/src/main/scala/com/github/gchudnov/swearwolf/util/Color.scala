package com.github.gchudnov.swearwolf.util

final case class Color(r: Int, g: Int, b: Int)

object Color {

  /**
   * Parses color from a string
   *
   * #RRGGBB
   * RRGGBB
   */
  def parse(value: String): Either[Throwable, Color] =
    if (value.isEmpty)
      Left(new ColorException("Cannot parse the color: <empty>"))
    else
      fromName(value).orElse(fromRGB(value))

  private def fromName(name: String): Either[Throwable, Color] = {
    require(name.nonEmpty, "color name must be non-empty")
    NamedColor
      .parse(name)
  }

  private def fromRGB(value: String): Either[Throwable, Color] = {
    require(value.nonEmpty, "color value must be non-empty")
    val c = if (value.head == '#') value.tail else value
    if (c.length != 6)
      Left(new ColorException("Cannot parse the color: invalid format. Supported formats: (#RRGGBB, RRGGBB)"))
    else {
      val cs = c.grouped(2).map(it => Integer.parseInt(it, 16)).toSeq
      Right(Color(cs(0), cs(1), cs(2)))
    }
  }
}
