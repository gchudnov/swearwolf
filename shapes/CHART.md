# CHART

When a chart is created, a `ChatStyle` can be used to specify the style: `Dot`, `Step`, `Quad`. 
The style encodes how many _levels_ one character of the chart can contain.

- **Dot** - one character has 2 levels in x-axis and 4 in y-axis, `⣿`. Might be used as a good default option.
- **Step** - one character has 1 level in x-axis and 8 in y-axis points, `▁▂▃▄▅▆▇█`. Might be used when a more smooth graph along y-axis is needed.
- **Quad** - one character has 2 levels in x-axis and 2 in y-axis, `▟`.

## ChartStyle.Dot

```scala
val chart = Chart(Size(8, 1), List(0.0, 24.0, 25.0, 50.0, 74.0, 75.0, 100.0), ChartStyle.Dot)
```

```text
     ⣠⣶⣿
```

## ChartStyle.Step

```scala
val chart = Chart(Size(10, 1), List(0.1, 12.5, 25.0, 37.5, 50.0, 62.5, 75.0, 87.5, 100.0), ChartStyle.Step)
```

```text
 ▁▂▃▄▅▆▇██
```

## ChartStyle.Quad

```scala
val chart = Chart(Size(5, 1), List(0.1, 12.5, 25.0, 37.5, 50.0, 62.5, 75.0, 87.5, 100.0), ChartStyle.Quad)
```

```text
▗▄▟██
```

## Examples

- [examples/either](../examples/either) - writes shapes to the terminal, using `Either[Throwable, *]` to wrap the result.