# swearwolf / shapes

> A collection of shapes

The following shapes are implemented in the library:

- [Box](./BOX.md)
- [Chart](./CHART.md)
- [Grid](./GRID.md)
- [Label](./LABEL.md)
- [Table](./TABLE.md)

The library extends [Screen](../util/README.md#Screen) with put operations to display the given shape in the terminal.

## Usage

Import the library

```scala
libraryDependencies += "com.github.gchudnov" %% "swearwolf-shapes" % "2.0.0"
```

import packages:

```scala
import com.github.gchudnov.swearwolf.shapes.*
import com.github.gchudnov.swearwolf.shapes.EitherShapes.*
```

And use the following functions for the rendering:

```scala
def putBox(pt: Point, box: Box, textStyle: TextStyle): F[Unit]
def putChart(pt: Point, chart: Chart, textStyle: TextStyle): F[Unit]
def putGrid(pt: Point, grid: Grid, textStyle: TextStyle): F[Unit]
def putLabel(pt: Point, label: Label, textStyle: TextStyle): F[Unit]
def putTable(pt: Point, table: Table, textStyle: TextStyle): F[Unit]
```

where:
- `Point` - point on the `Screen` to write the shape to.
- `Box`, `Chart`, `Grid`, `Label`, `Table` are the shapes to display.
- `TextStyle` - used to specify foreground / background styles of the text used to display the shape.
- `F[_]` - effect used to wrap the result, e.g. `Either[Throwable, *]`.

For more details, please refer to one of the provided examples.

## Examples

- [examples/either](../examples/either) - writes shapes to the terminal, using `Either[Throwable, *]` to wrap the result.
- [examples/zio](../examples/ziox) - integration with ZIO.
