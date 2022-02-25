= TODO: extract methods and remove draw module.


= TODO: finish shapes description
- TODO: add chart readme page
- TODO: update readme pages
- TODO: add Span = text + styles, render spans

- TODO: text should support \r\n
- https://docs.unity3d.com/Packages/com.unity.ugui@1.0/manual/StyledText.html

- TODO: Rich, Shapes -- common way to build, compile
- TODO: term should render Spans?

/*
private[table] trait TableOps:
  extension (screen: Screen)
    def put(pt: Point, table: Table, textStyle: TextStyle): Either[Throwable, Unit] =
      TableDrawer.draw(screen)(pt, table, textStyle)

    def put(pt: Point, table: Table): Either[Throwable, Unit] =
      put(pt, table, TextStyle.Empty)

private[draw] trait TableSyntax extends TableOps

object TableSyntax extends TableSyntax

*/