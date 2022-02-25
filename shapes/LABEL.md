# Label

## AlignStyle.Left

```scala
val label  = Label(Size(16, 1), "test data", AlignStyle.Left)
```

```text
|test data       |
```

If the text is longer than the width, it will be truncated.

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
