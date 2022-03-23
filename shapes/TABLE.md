# Table

A `Table` takes `Seq[Seq[Any]` where each element is converted to a `String`. `TableStyle` parameter is used to style borders of a table.

## TableStyle.Simple

```scala
val data = Seq(
  Seq[Any]("h1", "h2"),
  Seq[Any](10, "A"),
  Seq[Any](20, "B")
)

val table = Table(data, TableStyle.Simple)
```

```text
+----+----+
| h1 | h2 |
+----+----+
| 10 | A  |
| 20 | B  |
+----+----+
```

## TableStyle.Frame

```scala
val data = Seq(
  Seq[Any]("h1", "h2"),
  Seq[Any](10, "A"),
  Seq[Any](20, "B")
)

val table = Table(data, TableStyle.Frame)
```

```text
┌────┬────┐
│ h1 │ h2 │
├────┼────┤
│ 10 │ A  │
│ 20 │ B  │
└────┴────┘
```

## Examples

- [examples/either](../examples/either) - writes shapes to the terminal, using `Either[Throwable, *]` to wrap the result.
