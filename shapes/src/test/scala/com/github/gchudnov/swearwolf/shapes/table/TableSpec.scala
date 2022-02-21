package com.github.gchudnov.swearwolf.shapes.table

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.shapes.Resources
import com.github.gchudnov.swearwolf.shapes.table.{ Table, TableStyle }
import com.github.gchudnov.swearwolf.shapes.table.internal.TablePresenter
import zio.test.Assertion.*
import zio.test.*

object TableSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Table")(
      test("draw simple") {
        val data = Seq(
          Seq[Any]("h1", "h2"),
          Seq[Any](10, "A"),
          Seq[Any](20, "B")
        )

        val table = Table(data, TableStyle.Simple)

        val actual = TablePresenter.present(table).mkString("\n")
        val expected = Resources.string("table/table-simple.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("draw frame") {
        val data = Seq(
          Seq[Any]("h1", "h2"),
          Seq[Any](10, "A"),
          Seq[Any](20, "B")
        )

        val table = Table(data, TableStyle.Frame)

        val actual = TablePresenter.present(table).mkString("\n")
        val expected = Resources.string("table/table-frame.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("empty can be created") {
        val data  = Seq.empty[Seq[Any]]
        val table = Table(data, TableStyle.Frame)

        val actual = TablePresenter.present(table).mkString("\n")
        val expected = Resources.string("table/table-empty.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("size can be estimated if the input is non-empty") {
        val data = Seq(
          Seq[Any]("h1", "h2"),
          Seq[Any](10, "A"),
          Seq[Any](20, "B")
        )

        val table = Table(data, TableStyle.Frame)

        val actual   = table.estimateSize()
        val expected = Size(11, 6)

        assert(actual)(equalTo(expected))
      },
      test("size can be estimated if the input is empty") {
        val data       = Seq.empty[Seq[Any]]
        val tableStyle = TableStyle.Frame

        val table = Table(data, TableStyle.Frame)

        val actual   = table.estimateSize()
        val expected = Size(0, 0)

        assert(actual)(equalTo(expected))
      }
    )
