# swearwolf

<img src="res/images/swearwolf-256.png" width="256px" height="219px" align="right" />

> A low level Scala library to construct text user interfaces.

<br clear="right" /><!-- Turn off the wrapping for the logo image. -->

## Preview

![preview](res/images/preview.png)

## Usage

Add the following dependency to your `build.sbt`:

```scala
libraryDependencies += "com.github.gchudnov" %% "swearwolf" % "1.0.0"
```

import:

```scala
import com.github.gchudnov.swearwolf.Screen
import com.github.gchudnov.swearwolf.term._
import com.github.gchudnov.swearwolf.util.EventLoop._
import com.github.gchudnov.swearwolf.util.TextStyle._
import com.github.gchudnov.swearwolf.util._
```

In code, create an *instance of a screen* by calling `Screen.acquire()` or `Screen.acquireOrThrow()` functions.

Use the screen functions `screen.put(/* ... */)` to print text data on it and call `screen.flush()` to display it.

Close the screen with `screen.close()` method.

Optionally, one can set an *event handler* and start the *event loop* to listen for control (e.g. screen size change), keyboard and mouse events.

```scala
Using.resource(Screen.acquireOrThrow()) { (sc: Screen) =>
  val handler = /* (ks: List[KeySeq]) => Either[Throwable, EventLoop.Action] */
  for {
    _ <- render(sc)
    _ <- sc.eventLoop(EventLoop.withDefaultHandler(handler))
  } yield ()
}
```

## API

### Acquire / Release of the Screen

A `Screen` is a `Releasable` resource that is used to interact with the terminal.

#### Screen.acquire(): Either[Throwable, Screen]

Constructs an instance of the screen and returns an error or an instance.

#### def acquireOrThrow(): Screen

Constructs an instance of the Screen or throws an error if there is an exception.

### Size of the screen

#### screen.size: Size

Returns the size of the screen when default event handler is attached.


## Example

An example that depicts the library usage and keyboard handling.

To build an examppel, invoke:

```sbt
example/assembly
```

## Contact

[Grigorii Chudnov] (mailto:g.chudnov@gmail.com)


## License

Distributed under the [The MIT License (MIT)](LICENSE).
