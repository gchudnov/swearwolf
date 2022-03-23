# Label

A `Label` supports the `AlignStyle` parameter that can be used to specify the way the text is aligned in the given area.

## AlignStyle.Left

```scala
val label  = Label(Size(16, 1), "test data", AlignStyle.Left)
```

```text
|test data       |
```

If the text is longer than the provided width, it will be truncated.

```scala
val label = Label(Size(16, 2), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)
```

```text
|this is a very  |
|long text t...  |
```

## AlignStyle.Right

```scala
val label  = Label(Size(16, 1), "test data", AlignStyle.Right)
```

```text
|       test data|
```

## AlignStyle.Center

```scala
val label = Label(Size(16, 1), "test data", AlignStyle.Center)
```

```text
|   test data    |
```

## Examples

- [examples/either](../examples/either) - writes shapes to the terminal, using `Either[Throwable, *]` to wrap the result.
