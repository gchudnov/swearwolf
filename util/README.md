# swearwolf / util

> Utilities for swearwolf

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

- **Color.parseEither(value: String): Either[Throwable, Color]** - parses a color and returns the result as `Either[Throwable, Color]`.
- **Color.parseId(value: String): Identity[Color]** - parses a color and returns it or throws an exception.
- **Color.parseTry(value: String): Try[Color]** - parses a color and returns the result, wrapped in `Try`.
- **Color.parseFuture(value: String): Future[Color]** - parses a color and returns the result, wrapped in a `Future`.

`value: String` is the [name of the color](COLORS.md) or hex-representation, `#RRGGBB` or `RRGGBB`.

- [List of the named colors](COLORS.md)

## Examples

TODO: add example