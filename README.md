# swearwolf

<img src="res/images/swearwolf-256.png" width="256px" height="219px" align="right" />

> A Scala library to write to and read from the terminal.

![Scala CI](https://github.com/gchudnov/swearwolf/workflows/Scala%20CI/badge.svg)

Built for Scala 3.1. 

Use the [previous](https://github.com/gchudnov/swearwolf/tree/v1.0.2) version of the library for scala 2.13.

<br clear="right" /><!-- Turn off the wrapping for the logo image. -->

## Preview

![preview](res/images/preview.png)

## Usage

Add the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.github.gchudnov.swearwolf" %% "term" % "2.0.0"

// Optionally, include rich-text & shapes libraries
libraryDependencies += "com.github.gchudnov.swearwolf" %% "rich" % "2.0.0"
libraryDependencies += "com.github.gchudnov.swearwolf" %% "shapes" % "2.0.0"
```

ZIO-integration: instead of the imports listed above, use the ones with `zio` suffix:

```scala
libraryDependencies += "com.github.gchudnov.swearwolf" %% "term-zio" % "2.0.0"
libraryDependencies += "com.github.gchudnov.swearwolf" %% "rich-zio" % "2.0.0"
libraryDependencies += "com.github.gchudnov.swearwolf" %% "shapes-zio" % "2.0.0"
```

A Non-Interactive example:

```scala
import com.github.gchudnov.swearwolf.rich.instances.IdRichText.*
import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.term.writers.IdWriter
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.geometry.*

object Main extends App:
  import TextStyle.*

  val term   = Term.syncId()
  val writer = IdWriter.make(term)

  val rich = RichText("<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>\n")

  writer.put("HELLO ", Bold | Foreground(Color.Blue))
  writer.put("WORLD!\n", Foreground(Color.Blue) | Background(Color.Yellow))
  writer.putRich(rich)

  writer.flush()
```

See [examples](./examples) directory for the list of demo applications. 

### Modules

- [/util](util) - A collection of utilities, other libraries in the project depend on.
- [/term](term) - Enable interactive and non-interactive IO in the terminal.
- [/shapes](shapes) - A collection of shapes (box, chart, grid, label, table) to use with the library.
- [/rich](rich) - Rich text to display in the terminal.
- [/zio](ziox) - Integration with ZIO.


## Building Examples

The project contains several demo applications that demonstrate how to use the library with the keyboard and mouse input.

To build, invoke:

```sbt
sbt assembly
```

After building, several executables will be available in  `/target` directory:

```bash
./example-either
./example-log
./example-noninteractive
./example-zio
```

## Compatibility

**The library was built and tested on Ubuntu 18.04.5 LTS with GNU bash, version 4.4.20**.


## Contact

[Grigorii Chudnov] (mailto:g.chudnov@gmail.com)


## License

Distributed under the [The MIT License (MIT)](LICENSE).
