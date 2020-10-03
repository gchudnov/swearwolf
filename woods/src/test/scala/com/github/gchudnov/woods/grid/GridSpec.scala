package com.github.gchudnov.woods.grid

import com.github.gchudnov.swearwolf.term.ArrayScreen
import com.github.gchudnov.swearwolf.util.{ Point, Size }
import com.github.gchudnov.woods.Resources
import zio.test.Assertion._
import zio.test._

object GridSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("Grid")(
      test("cell is too big for a grid") {
        val screen = ArrayScreen(Size(32, 32))
        val grid   = Grid(Size(1, 1), Size(3, 3), GridStyle.Dash2)

        val actual = screen.put(Point(0, 0), grid)

        val actualData   = screen.toString
        val expectedData = Resources.string("grid/grid-cell-too-big.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("draw dash2") {
        val screen = ArrayScreen(Size(32, 32))
        val grid   = Grid(Size(10, 10), Size(3, 3), GridStyle.Dash2)

        val actual = screen.put(Point(0, 0), grid)

        val actualData   = screen.toString
        val expectedData = Resources.string("grid/grid-dash2.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("draw frame") {
        val screen = ArrayScreen(Size(32, 32))
        val grid   = Grid(Size(10, 10), Size(3, 3), GridStyle.Frame)

        val actual = screen.put(Point(0, 0), grid)

        val actualData   = screen.toString
        val expectedData = Resources.string("grid/grid-frame.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("empty") {
        val screen = ArrayScreen(Size(32, 32))
        val grid   = Grid(Size(0, 0), Size(3, 3), GridStyle.Frame)

        val actual = screen.put(Point(0, 0), grid)

        val actualData   = screen.toString
        val expectedData = Resources.string("grid/grid-empty.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      }
    )
}
