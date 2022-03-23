# swearwolf / util

> Utilities for swearwolf

- [Color](#Color)
- [TextStyle](#TextStyle)
- [AlignStyle](#AlignStyle)

## Color

```scala
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.instances.*
import scala.util.Try

val errOrColor1: Either[Throwable, Color] = Color.parseEither("#ff1122")
val errOrColor2: Try[Color] = Color.parseTry("red")

val color: Color = Color.White
```

A `Color` is represented as a triple of **R**, **G** and **B** values in range `[0 - 255]`: `Color(r: Int, g: Int, b: Int)`.
Colors can be constructed by calling `Color.parseXXX` or by specifying the exact values of R, G and B.

```scala
  // Parses a color and returns the result as `Either[Throwable, Color]`
  Color.parseEither(value: String): Either[Throwable, Color]
  
  // Parses a color and returns it or throws an exception.
  Color.parseId(value: String): Identity[Color]

  // Parses a color and returns the result, wrapped in `Try`
  Color.parseTry(value: String): Try[Color]

  // Parses a color and returns the result, wrapped in a `Future`.
  Color.parseFuture(value: String): Future[Color] 
```

Where `value: String` is the [name of the color](COLORS.md) or hex-representation, `#RRGGBB` or `RRGGBB`.

- [List of colors](COLORS.md)

## TextStyle

`TextStyle` is a combination of one or more of the following styles:

- **TextStyle.Empty**

  No style is specified. The default text foreground and background colors are used.

- **TextStyle.Foreground(color: Color)**

  Specifies a foreground [color](#Color) of the text.

- **TextStyle.Background(color: Color)**

  Specifies a background [color](#Color) of the text.

- **TextStyle.Bold**

  Make letters of a text thicker than the surrounding text.

- **TextStyle.Italic**

  Text is written in a script style.

- **TextStyle.Underline**

  Underlines text.

- **TextStyle.Blink**

  Make text blinking on the screen.

- **TextStyle.Invert**

  Foreground and background colors of the text are inverted.

- **TextStyle.Strikethrough**

  Draws a line through the given text.

Text Styles can be composed using `|` operator, e.g.:

```scala
val textStyle = TextStyle.Foreground(NamedColor.Azure) | TextStyle.Background(NamedColor.LightGray) | TextStyle.Strikethrough
```

## AlignStyle

Allows to specify the text alignment:

- `AlignStyle.Left`
- `AlignStyle.Right`
- `AlignStyle.Center`

## Examples

- [examples/colors](../examples/colors) - generates PNG-images of the available colors.
