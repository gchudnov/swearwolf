# GRID

A `Grid` is constructed by providing a total size, cell-size and `GridStyle` to specify borders to use.

## GridStyle.Dash2

```scala
val grid = Grid(Size(10, 10), Size(3, 3), GridStyle.Dash2)
```

```text
┌╌╌┬╌╌┬╌╌┐
╎  ╎  ╎  ╎
╎  ╎  ╎  ╎
├╌╌┼╌╌┼╌╌┤
╎  ╎  ╎  ╎
╎  ╎  ╎  ╎
├╌╌┼╌╌┼╌╌┤
╎  ╎  ╎  ╎
╎  ╎  ╎  ╎
└╌╌┴╌╌┴╌╌┘
```

## GridStyle.Frame

```scala
val grid = Grid(Size(10, 10), Size(3, 3), GridStyle.Frame)
```

```text
┌──┬──┬──┐
│  │  │  │
│  │  │  │
├──┼──┼──┤
│  │  │  │
│  │  │  │
├──┼──┼──┤
│  │  │  │
│  │  │  │
└──┴──┴──┘
```

## Examples

- [examples/either](../examples/either) - writes shapes to the terminal, using `Either[Throwable, *]` to wrap the result.
