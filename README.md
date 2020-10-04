# swearwolf

<img src="res/images/swearwolf-256.png" width="256px" height="219px" align="right" />

> A low level Scala library to construct text user interfaces.

Using this library, one can create full-screen text-based applications.
The library sends the correct control characters to the terminal to display text in the given style.

The additional library of UI-primitives, *woods* can be used to display basic elements on the screen:
a *box*, *graph*, *grid*, *label*, *table* and *rich-text*.

<br clear="right" /><!-- Turn off the wrapping for the logo image. -->

## Preview

![preview](res/images/preview.png)

## Usage

Add the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.github.gchudnov" %% "swearwolf" % "1.0.0"
```

Optionally, include the library of the UI-primitives:

```scala
libraryDependencies += "com.github.gchudnov" %% "swearwolf-woods" % "1.0.0"
```

This optional library contains a set of elelemts: *box*, *graph*, *grid*, *label*, *table* and *rich-text* to render on the `Screen`.

In the application, import:

```scala
import com.github.gchudnov.swearwolf._
import com.github.gchudnov.swearwolf.util._
```

Next, create an *instance of a screen* by calling `Screen.acquire()` or `Screen.acquireOrThrow()` functions.

Use the screen functions `screen.put(/* ... */)` to print text data and invoke `screen.flush()` to flush the printed data to terminal.

At the end, close the screen with `screen.close()` call.

Optionally, one can set an *event handler* and start an *event loop* to listen for control- (e.g. screen size change), keyboard- and mouse- events.

A minimal example:

```scala
Using.resource(Screen.acquireOrThrow()) { (sc: Screen) =>
  val handler = /* (ks: List[KeySeq]) => Either[Throwable, EventLoop.Action] */
  for {
    _ <- sc.put("Hello World!")
    _ <- sc.eventLoop(EventLoop.withDefaultHandler(handler))
  } yield ()
}
```

## API

### Acquire / Release the Screen

A `Screen` is a [Releasable](https://www.scala-lang.org/api/current/scala/util/Using$$Releasable.html) resource that is used to interact with the terminal.

- **Screen.acquire(): Either[Throwable, Screen]**

  Constructs an instance of the screen and returns an error or an instance of the Screen.

- **Screen.acquireOrThrow(): Screen**

  Constructs an instance of the Screen or throws an error if there is an exception.


### Screen interface

A Screen interface contains a list of functions to display text on the terminal and control it.

- **def size: Size**

  Returns the current size of the screen (width, height). When the size of the terminal changes, the value is updated.

  NOTE: the size of the screen is updated only when either `eventLoop` or `eventPoll` is called (see the description of these functions below). In other words, if no handler handles events from the terminal, the screen `size` is not updated.

- **def put(pt: Point, value: String): Either[Throwable, Unit]**

  Displays a given test value at the given point of the screen.
  
  NOTE: Point(0, 0) represents the top-left corner of the screen.

- **def put(pt: Point, value: String, style: Style[Text]): Either[Throwable, Unit]**

  Displays a given test value at the given point of the screen with the given [text style](#Text-Styles). After the text is written with the provided styles, these styles are reset.

- **def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit]**

  Displays a given array of bytes at the given point of the screen.

- **def cursorHide(): Either[Throwable, Unit]**

  Hides the cursor on the screen.

- **def cursorShow(): Either[Throwable, Unit]**

  Displays the cursor on the screen.

- **def mouseTrack(): Either[Throwable, Unit]**

  Enables mouse coordinates tracking. Coordinates are reported when a mouse button is clicked.

- **def mouseUntrack(): Either[Throwable, Unit]**

  Disables mouse tracking.

- **def bufferNormal(): Either[Throwable, Unit]**

  Chnages terminal buffer to normal.

- **def bufferAlt(): Either[Throwable, Unit]**

  Changes terminal buffer to the alternative one.

- **def clear(): Either[Throwable, Unit]**

  Clears the screen.

- **def flush(): Either[Throwable, Unit]**

  Flushes draw operations accumlated in the buffer to terminal, to make them effective. For example after making any of the `put()`, `clear()`, `mouseTrack() / mouseUntrack()` calls a flush is needed. Otherwise some of the operations might be still buffered and not effective.

- **def init(): Either[Throwable, Unit]**

  Runs `clear()`, `cursorHide()`, `mouseTrack()` and other internal function to initialize the screen. This function is called *automatically* when the screen is created via one of the `Screen.acquire` operations.

- **def shutdown(): Either[Throwable, Unit]**

  Reverts all operations that were executed in `init()`. The method is called automatically when `close()` method is invoked.

- **def eventLoop(handler: KeySeqHandler): Either[Throwable, Unit]**

  Starts the event loop with the given [key sequence handler](#Key-Sequence-Handler).
  Event loop is responsible for handling keyboard-, mouse-, screen-size- events. The default implementation of `eventLoop` with the default event handler returns only when `ESC` is pressed.

- **def eventLoop(): Either[Throwable, Unit]**

  Starts the event loop with the default event handler: `EventLoop.defaultHandler`. The default event handler handles screen resize events and listens for the `ESC` button to be pressed. After that `eventLoop` exits and control is returned to the code that invoked `eventLoop` method.

- **def eventPoll(): Either[Throwable, List[KeySeq]]**

  Polls for new events and returns them as a list of [Key Sequence Events](#Key-Sequence-Events)

- **def close(): Unit**

  Closes the screen. The default implementation calls the `shutdown()` method and throws an error if `shutdown` fails.


### Key Sequence Events

Key Seqnence trait, `KeqSeq` represents a control event received from the terminal.
The following Key Sequences extends `KeySeq` and are propagated to the event handler in the application:

- **case class SizeKeySeq(sz: Size) extends KeySeq**

  Represents a new terminal size after window resize.

- **case class CharKeySeq(ch: Char, mods: Set[KeyModifier]) extends KeySeq**

  Represents a text character with a set of key modifers.

- **case class CtrlKeySeq(key: KeyCode, mods: Set[KeyModifier]) extends KeySeq**

  Represents a control ket code, e.g. `F1` with a set of key modifiers.

- **case class MouseKeySeq(pt: Point, button: MouseButton, action: MouseAction, mods: Set[KeyModifier]) extends KeySeq**

  Representa a captured mouse event when the button was pressed or released.

- **case class UnfamiliarKeySeq(bytes: Seq[Byte]) extends KeySeq**

`KeyModifier` is a control key that was pressed: `Shift`, `Alt` or `Ctrl`.

`MouseButton` is one of the following events: `Left`, `Middle`, `Right`, `ScrollBackward` or `ScrollForward`.

`MouseAction` is either `Press` or `Release` event.

### Key Sequence Handler

Key Sequence Handler, `KeySeqHandler` has the following signature:

```scala
type KeySeqHandler = List[KeySeq] => Either[Throwable, EventLoop.Action]
```

it is used insde of the `eventLoop` method of a screen and and used to process a collection of key sequences, `KeySeq`.
For example, the processing could include printing something on the screen, when a keyboard key is pressed.

After processing a function should return `EventLoop.Action` that is either `Action.Continue` if we want to continue running the event loop, or
`Action.Exit` to exit the event loop.

There is a default event handler, `EventLoop.defaultHandler` that returns `Action.Exit` when `ESC` key is pressed.

`EventLoop.withDefaultHandler(handler: KeySeqHandler): KeySeqHandler` can be used to compose a user-provided event handler with the default one.
Both handlers are composed in a way that if one of them returns `Action.Exit` for the provided input, the resulting return value is `Action.Exit`.


### Text Styles

`TextStyle` is a combination of one or more of the following values:

- **TextStyle.Empty**

  No style is specified. The default text foreground and background colors are used.

- **TextStyle.Foreground(color: Color)**

  Specifies a foreground [color](#Colors) of the text.

- **TextStyle.Background(color: Color)**

  Specifies a background [color](#Colors) of the text.

- **TextStyle.Bold**

  Makes letters of a text thicker than the surrounding text.

- **TextStyle.Italic**

  Text is written in a script style.

- **TextStyle.Underline**

  Draws lines under letters for emphasis.

- **TextStyle.Blink**

  Text is blinking on the screen.

- **TextStyle.Invert**

  Foreground and background colors of the text are inverted.

- **TextStyle.Strikethrough**

  Draws a line throush the given text.

### Colors

A `Color` is a combination of **R**, **G** and **B** values in range `[0 - 255]` and represented as: `Color(r: Int, g: Int, b: Int)`
Colors can be constructed by calling `Color.parse` or by specifying the exact values of R, G and B values.

- **Color.parse(value: String): Either[Throwable, Color]**

  Parses a color specified in `#RRGGBB` or `RRGGBB` format where `RR`, `GG`, `BB` is a hex value.
  For example: `#FFFFFF` and `FFFFFF` are valid colors representing white.

In addition, a library defined a set of named colors, `NamedColor` that can be used to create a `Color` instance.
Named colors can be used directly, e.g. `NamedColor.Aqua` or `NamedColor.LightCyan` or parsed from a text value: `NamedColor.parse`

- **NamedColor.parse(name: String): Either[Throwable, Color]**

  Here a string is the name of the color separated by '-' for composite words, e.g.: `aqua` or `light-cyan`.

The [list of named colors](res/colors/NAMED-COLORS.md) supported by the library.

## Woods - a library with basic Primitives

`Woods` library prvides basic building blocks or primitives for rendering on the screen.

The collection of available primitives includes: a *box*, *graph*, *grid*, *label*, *table* and *rich-text*.

In the current implementation of the library these primitives are very basic and cannot be composed. In this way it is not possible to create a table where a cell includes a label or a box.

### Primitives Description

#### Box

Extends a `Screen` with a `put` function that alows to draw a box on the screen:

```scala
def put(pt: Point, box: Box, textStyle: TextStyle): Either[Throwable, Unit]
```

When a box is constructed, an additional `BoxStyle` can be used to specify the kind of borders to use: `Empty`, `SingleBorder`, `DoubleBorder`, `BoldBorder`.

#### Graph

Extends a `Screen` with a `put` function that alows to draw a graph on the screen:

```scala
def put(pt: Point, graph: Graph, textStyle: TextStyle): Either[Throwable, Unit]
```

When a graph is constructed, an additional `GraphStyle` can be used to specify the kind of graph to use: `Dot`, `Step`, `Quad`. The style of the graph encodes how many values can one character of the text contain.

- **Dot** - one text character contains 2-x and 4-y levels.
- **Step** - one text character contains 1-x and 8-y levels.
- **Quad** - one text character contains 2-x and 2-y levels.

#### Grid

Extends a `Screen` with a `put` function that alows to draw a grid on the screen:

```scala
def put(pt: Point, grid: Grid, textStyle: TextStyle): Either[Throwable, Unit]
```

`Grid` is constructed with a cell-size and `GridStyle` to specify the set of borders and intersectors to use when drawing this primitive.

#### Label

Extends a `Screen` with a `put` function that alows to draw a label on the screen:

```scala
def put(pt: Point, label: Label, textStyle: TextStyle): Either[Throwable, Unit]
```

Label provides the `AlignStyle` that can be used to specify the way the text is aligned inside of the label.

#### Table

Extends a `Screen` with a `put` function that alows to draw a table on the screen:

```scala
def put(pt: Point, table: Table, textStyle: TextStyle): Either[Throwable, Unit]
```

A table takes `Seq[Seq[Any]` where each cell is converted to a string and displayed. `TableStyle` can be used to style the borders of a table.

#### RichText

Extends a `Screen` with a `put` function that alows to draw a rich text on the screen:

```scala
def put(pt: Point, value: RichText): Either[Throwable, Unit]
```

`RichText` can be constructed using a html-like tags and their combinations:

- **bold**, **b**

  ```html
  <bold>TEXT</bold> / <b>TEXT</b>
  ```

- **italic**, **i**

  ```html
  <italic>TEXT</italic> / <i>TEXT</i>
  ```

- **underline**, **u**

  ```html
  <underline>TEXT</underline> / <u>TEXT</u>
  ```

- **blink**, **k**

  ```html
  <blink>TEXT</blink> / <k>TEXT</k>
  ```

- **invert**, **v**

  ```html
  <invert>TEXT</invert> / <v>TEXT</v>
  ```

- **strikethrough**, **t**

  ```html
  <strikethrough>TEXT</strikethrough> / <t>TEXT</t>
  ```

- **color**
  specifies a color using the following attributes:
  - **fgcolor**, **fg** to specify the foreground color of the text.
  - **bgcolor**, **bg** to specify the background color of the text.
  
  ```html
  <color fg='#FF0000' >TEXT</color> / <color fgcolor='#FF0000' >TEXT</color>
  <color bg='#00FF00' >TEXT</color> / <color bgcolor='#00FF00' >TEXT</color>
  <color fg='#FF0000' bg='#00FF00' >TEXT</color> / <color fgcolor='#FF0000' bg='#00FF00' >TEXT</color>
  ```

A more complex example:

```html
<b>BOLD</b><color fg='#AA0000' bg='#00FF00'>NOR</color>MAL<i>italic</i><k>BLINK</k>
```

Text and tags can be nested, for example:

```html
<i>A<b>text</b>B<u>C</u></i>
```

## Example

The project contains an example that depicts the library usage and keyboard / mouse handling.

To build an example application, invoke:

```sbt
sbt example/assembly
```

The executable file will be built and saved in `target `directory.

## NOTE

**The library was built and tested on Ubuntu 18.04.5 LTS with GNU bash, version 4.4.20**. There is a chance that the library won't be working on a different OS or/and terminals.

## Contact

[Grigorii Chudnov] (mailto:g.chudnov@gmail.com)


## License

Distributed under the [The MIT License (MIT)](LICENSE).
