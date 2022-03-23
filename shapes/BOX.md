# Box

A `BoxStyle` can be used to specify a border to use: `Empty`, `SingleBorder`, `DoubleBorder`, `BoldBorder`.

## BoxStyle.SingleBorder

```scala
val box = Box(Size(20, 10), BoxStyle.SingleBorder)
```

```text
┌──────────────────┐
│                  │
│                  │
│                  │
│                  │
│                  │
│                  │
│                  │
│                  │
└──────────────────┘
```

## BoxStyle.DoubleBorder

```scala
val box    = Box(Size(20, 10), BoxStyle.DoubleBorder)
```

```text
╔══════════════════╗
║                  ║
║                  ║
║                  ║
║                  ║
║                  ║
║                  ║
║                  ║
║                  ║
╚══════════════════╝
```

## BoxStyle.BoldBorder

```scala
val box = Box(Size(20, 10), BoxStyle.BoldBorder)
```

```text
▛▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▜
▌                  ▐
▌                  ▐
▌                  ▐
▌                  ▐
▌                  ▐
▌                  ▐
▌                  ▐
▌                  ▐
▙▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▟
```


## Examples

- [examples/either](../examples/either) - writes shapes to the terminal, using `Either[Throwable, *]` to wrap the result.
