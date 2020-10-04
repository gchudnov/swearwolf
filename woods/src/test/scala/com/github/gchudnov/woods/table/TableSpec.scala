package com.github.gchudnov.woods.table

import com.github.gchudnov.swearwolf.term.ArrayScreen
import com.github.gchudnov.swearwolf.util.{Point, Size}
import com.github.gchudnov.woods.{Resources, Table, TableStyle}
import zio.test.Assertion._
import zio.test._

object TableSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("Table")(
      test("draw simple") {
        val screen = ArrayScreen(Size(32, 32))

        val data = Seq(
          Seq[Any]("h1", "h2"),
          Seq[Any](10, "A"),
          Seq[Any](20, "B")
        )

        val table = Table(data, TableStyle.Simple)

        val actual = screen.put(Point(0, 0), table)

        val actualData   = screen.toString
        val expectedData = Resources.string("table/table-simple.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("draw frame") {
        val screen = ArrayScreen(Size(32, 32))

        val data = Seq(
          Seq[Any]("h1", "h2"),
          Seq[Any](10, "A"),
          Seq[Any](20, "B")
        )

        val table = Table(data, TableStyle.Frame)

        val actual = screen.put(Point(0, 0), table)

        val actualData   = screen.toString
        val expectedData = Resources.string("table/table-frame.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("empty can be created") {
        val screen = ArrayScreen(Size(32, 32))

        val data  = Seq.empty[Seq[Any]]
        val table = Table(data, TableStyle.Frame)

        val actual = screen.put(Point(0, 0), table)

        val actualData   = screen.toString
        val expectedData = Resources.string("table/table-empty.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("size can be estimated if the input is non-empty") {
        val data = Seq(
          Seq[Any]("h1", "h2"),
          Seq[Any](10, "A"),
          Seq[Any](20, "B")
        )

        val tableStyle = TableStyle.Frame

        val actual   = Table.estimateSize(data, tableStyle)
        val expected = Size(11, 6)

        assert(actual)(equalTo(expected))
      },
      test("size can be estimated if the input is empty") {
        val data       = Seq.empty[Seq[Any]]
        val tableStyle = TableStyle.Frame

        val actual   = Table.estimateSize(data, tableStyle)
        val expected = Size(0, 0)

        assert(actual)(equalTo(expected))
      }
    )
}
