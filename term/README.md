# swearwolf / term

> Terminal for swearwolf

## Usage

1. Create a Term instance
2. Create a `Screen` (Interactive) or `Writer` (Non-Interactive Mode)
3. Create an `EventLoop` to handle events from StdIn (optional)

### 1. Create a Term instance

```scala
import com.github.gchudnov.swearwolf.term.terms.Term

val term = Term.syncEither()
```

`Term` contains a set of methods that can be used to interact on low-level with the Terminal.

```scala
trait Term[F[_]]:
  def read(): F[Option[Array[Byte]]]
  def write(bytes: Array[Byte]): F[Unit]
  def flush(): F[Unit]
  def close(): F[Unit]
```

It is not advised to use `Term` directly, but rather via `Writer` or `Screen` that act as a front-end for the terminal and have methods to display styled text.

### 2. Interactive / Non-Interactive Mode

The application can either Non-Interactive or Interactive Mode:
- - *Non-Interactive Mode* allows writing rich-text and shapes to StdOut, and `StdIn` is not processed by the library.
- *Interactive Mode* captures input from the keyboard and mouse and an `EventLoop` is used to process it.

#### 2A. Non-Interactive Mode

Create an instance of `Writer`. A writer is used to write styled text to the terminal.

```scala
import com.github.gchudnov.swearwolf.term.writers.IdWriter

val writer = IdWriter.make(term)
```

```scala
trait Writer[F[_]]:
  def put(value: String): F[Unit]
  def put(value: String, style: TextStyle): F[Unit]
  def put(value: Span): F[Unit]
  def put(value: Array[Byte]): F[Unit]

  def flush(): F[Unit]
```

Here [TextStyle](../util/README.md#TextStyle) is used to specify the style of the text, [Span](../util/README.md#Span) allows the creation of rich-text strings.

Import [rich](../rich) library to expend the methods available in the `Screen` interface.

#### 2B. Interactive Mode

Create an instance of `Screen` and `EventLoop`.

```scala
import com.github.gchudnov.swearwolf.term.screens.EitherScreen
import com.github.gchudnov.swearwolf.term.eventloops.EitherEventLoop

val screen: Either[Throwable, Screen] = EitherScreen.make(term)
val eventLoop: Either[Throwable, EventLoop] = EitherEventLoop.make(term)
```

A `Screen` allows to print style text at the given coordinates in the Terminal.

```scala
trait Screen[F[_]] extends Writer[F]:
  def put(pt: Point, value: String): F[Unit]
  def put(pt: Point, value: String, style: TextStyle): F[Unit]
  def put(pt: Point, value: Span): F[Unit]
  def put(pt: Point, value: Array[Byte]): F[Unit]

  def close(): F[Unit]
```

Import [rich](../rich) and [shapes](../shapes) libraries to expend the methods available in the `Screen` interface.

## 3. Event Loop

An `EventLoop` is needed to read keyboard and mouse input as key sequences.

```scala
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.Action
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler
import com.github.gchudnov.swearwolf.term.*
import com.github.gchudnov.swearwolf.term.eventloops.EitherEventLoop

val eventLoop: Either[Throwable, EventLoop] = EitherEventLoop.make(term)

val keySeqHandler: KeySeqHandler[Either[Throwable, *]] = (ks: KeySeq) =>
  if (ks.isEsc) then Right(EventLoop.Action.Exit)
  else Right(Action.Continue)

val _: Either[Throwable, Unit] = eventLoop.run(keySeqHandler)
```

A Key Sequence Handler has the following signature:

```scala
type KeySeqHandler[F[_]] = KeySeq => F[EventLoop.Action]
```

It is provided to the `eventLoop` to handle the incoming key sequences, `KeySeq` and trigger actions.
For example, when a keyboard key is pressed, a corresponding key sequence is passed to the provided `KeySeqHandler` in the event loop.

After processing the event handler should return one of following EventLoop Actions:

- `Action.Continue` - to continue running the event loop,
- `Action.Exit` - to exit the event loop.

## Key Sequences

Key Sequence trait, `KeqSeq` represents a control event received from the Terminal.
These events might be processed with an instance of the `EventLoop`, connected to the terminal.

The following Key Sequences are available:

- **case class SizeKeySeq(sz: Size) extends KeySeq**

  Raised when the terminal window was resized. `Size` contains the new *width* and *height* of the screen.

- **case class CharKeySeq(ch: Char, mods: Set[KeyModifier]) extends KeySeq**

  Represents a text character with a set of key modifiers.
  `KeyModifier` is a control key: `Shift`, `Alt` or `Ctrl`.

- **case class CtrlKeySeq(key: KeyCode, mods: Set[KeyModifier]) extends KeySeq**

  Represents a control ket code, e.g. `F1` with a set of key modifiers.
  `KeyModifier` is a control key: `Shift`, `Alt` or `Ctrl`.

- **case class MouseKeySeq(pt: Point, button: MouseButton, action: MouseAction, mods: Set[KeyModifier]) extends KeySeq**

  Represents a captured mouse event when a mouse button was pressed or released.
  `MouseButton` is one of the following events: `Left`, `Middle`, `Right`, `ScrollBackward` or `ScrollForward`.
  `MouseAction` is either a `Press` or `Release` event.

- **case class UnknownKeySeq(bytes: Seq[Byte]) extends KeySeq**

  Represents an unknown input that the library failed to parse.
  When the library reports this key sequence, most likely one of the parsers is incomplete on the given OS / terminal and cannot parse byte sequence that was sent to the library.

TODO: add logging

## Examples

- [examples/noninteractive](../examples/noninteractive) - writes RichText to stdout without capturing IO.
- [examples/either](../examples/either) - writes RichText to the terminal in interactive mode, using `Either[Throwable, *]` to wrap the result.
- [examples/zio](../examples/ziox) - integration with ZIO.
