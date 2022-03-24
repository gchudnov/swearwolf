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

---------------------------------------------------------------------------------------------------------


TODO: logging


???????????????


Next, create an *instance of the screen* by calling `Screen.acquire()` or `Screen.acquireOrThrow()` methods.

Use methods `screen.put(/* ... */)` to print text on the screen and invoke `screen.flush()` to flush data to the terminal.
At the end, call `screen.close()` to close the screen.

To listen for control- (e.g. screen size change), keyboard- and mouse- events, set an *event handler* and start an *event loop*.
Event Haldner and Event Loop are optional and might be needed only for interactive applications.

An example:

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

  Constructs an instance of the screen.

- **Screen.acquireOrThrow(): Screen**

  Constructs an instance of the Screen; Throws an Rxception if there is an error occured.


### Screen interface

Screen interface contains a list of methods to display text on the terminal and control it.

- **def size: Size**

  Returns the current size of the screen *(width, height)*.

  NOTE: the size of the screen is updated only if either `eventLoop` or `eventPoll` are called (see the description of these methods below).

- **def put(pt: Point, value: String): Either[Throwable, Unit]**

  Displays the given text at the specified position of the screen. NOTE: Point(0, 0) represents the top-left corner of the screen.

- **def put(pt: Point, value: String, style: TextStyle): Either[Throwable, Unit]**

  Displays the given text at the specified position of the screen with the given [text style](#Text-Styles).

- **def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit]**

  Displays the given array of bytes at the specified position of the screen.

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

  Flushes buffered draw operations to the terminal. After invoking `put()`, `clear()`, `mouseTrack()` / `mouseUntrack()`, etc. calls a flush is needed. Otherwise some of the operations might be still buffered in the application and not reflected on the screen.

- **def init(): Either[Throwable, Unit]**

  Initializes the screen. This method is called *automatically* when the screen is acquired using `Screen.acquire` or `Screen.acquireOrThrow` operations. When called, it runs `clear()`, `cursorHide()`, `mouseTrack()` and other internal function to initialize the screen.

- **def shutdown(): Either[Throwable, Unit]**

  Reverts all operations that were executed in `init()`. The method is called automatically when `close()` method is invoked.

- **def eventLoop(handler: KeySeqHandler): Either[Throwable, Unit]**

  Starts the event loop with the given [key sequence handler](#Key-Sequence-Handler).
  Event loop is responsible for handling keyboard-, mouse-, screen-size- events using the provided event handler. If the default handler is provided to the event loop (`EventLoop.defaultHandler`) the event loop exists only when `ESC` button is pressed.

- **def eventLoop(): Either[Throwable, Unit]**

  Starts the event loop with the default event handler: `EventLoop.defaultHandler`. It handles screen resize events and waits for the `ESC` button to be pressed to exit the event loop.

- **def eventPoll(): Either[Throwable, List[KeySeq]]**

  Polls for new events and returns them as a list of [Key Sequence Events](#Key-Sequence-Events)

- **def close(): Unit**

  Closes the screen. The default implementation calls the `shutdown()` method and throws an error if `shutdown` fails.


### Key Sequence Events



### Key Sequence Handler

Key Sequence Handler, `KeySeqHandler` has the following signature:

```scala
type KeySeqHandler = List[KeySeq] => Either[Throwable, EventLoop.Action]
```

it is used in the `eventLoop` to process the incoming key sequences, `KeySeq` and trigger some actions.
For example, when a keyboard key is pressed, a key sequence is generated that is passed to the `KeySeqHandler` in the event loop.

After processing the event handler should return one of two `EventLoop.Action`s:

- `Action.Continue` if we want to continue running the event loop,
- `Action.Exit` to exit the event loop.

The default event handler, `EventLoop.defaultHandler` returns `Action.Exit` only when `ESC` key is pressed.

To compose a user-provided event handler with the default one, `EventLoop.withDefaultHandler(handler: KeySeqHandler): KeySeqHandler` can be used.
This method always invokes both handlers (one provided by the user and the default one) and returns `Action.Exit` if any of the handlers returned `Action.Exit`.
