package com.github.gchudnov.swearwolf.shapes.grid

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.shapes.grid.{ Grid, GridStyle }
import com.github.gchudnov.swearwolf.shapes.grid.internal.GridPresenter
import com.github.gchudnov.swearwolf.shapes.Resources
import zio.test.Assertion.*
import zio.test.*

object GridSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Grid")(
      test("cell is too big for a grid") {
        val grid   = Grid(Size(1, 1), Size(3, 3), GridStyle.Dash2)

        val actual = GridPresenter.present(grid).mkString("\n")
        val expected = Resources.string("grid/grid-cell-too-big.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("draw dash2") {
        val grid   = Grid(Size(10, 10), Size(3, 3), GridStyle.Dash2)

        val actual = GridPresenter.present(grid).mkString("\n")
        val expected = Resources.string("grid/grid-dash2.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("draw frame") {
        val grid   = Grid(Size(10, 10), Size(3, 3), GridStyle.Frame)

        val actual = GridPresenter.present(grid).mkString("\n")
        val expected = Resources.string("grid/grid-frame.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("empty") {
        val grid   = Grid(Size(0, 0), Size(3, 3), GridStyle.Frame)

        val actual = GridPresenter.present(grid).mkString("\n")
        val expected = Resources.string("grid/grid-empty.txt").toTry.get

        assert(actual)(equalTo(expected))
      }
    )
