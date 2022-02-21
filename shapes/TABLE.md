# Table

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
