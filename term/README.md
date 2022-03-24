# swearwolf / term

> Terminal for swearwolf

## Usage

### 1. Create a Term instance

```scala
import com.github.gchudnov.swearwolf.term.terms.EitherSyncTerm

val term = EitherSyncTerm.make()
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

### 2A. Non-Interactive Mode

Create an instance of `Writer`.

```scala

```

```scala

```

Import [rich](../rich) library to expend the methods available in the `Screen` interface.


#### 2B. Interactive Mode

Create an instance of `Screen` and `EventLoop`.

```scala

```

```scala

```

Import [rich](../rich) and [shapes](../shapes) libraries to expend the methods available in the `Screen` interface.

## Event Loop



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


## Examples

- [examples/noninteractive](../examples/noninteractive) - writes RichText to stdout without capturing IO.
- [examples/either](../examples/either) - writes RichText to the terminal in interactive mode, using `Either[Throwable, *]` to wrap the result.
- [examples/zio](../examples/ziox) - integration with ZIO.
