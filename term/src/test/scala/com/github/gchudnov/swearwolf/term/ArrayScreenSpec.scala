package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.Resources
import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import zio.test.Assertion.*
import zio.test.*

object ArrayScreenSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("ArrayScreen")(
      test("text is drawn below the screen") {
        val screen = ArrayScreen(Size(32, 32))

        val actual = screen.put(Point(0, 40), "TEXT")

        val actualData   = screen.toString
        val expectedData = Resources.string("term/array-screen-draw-below.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("text is drawn on the right edge of the screen") {
        val screen = ArrayScreen(Size(32, 32))

        val actual = screen.put(Point(30, 0), "TEXT")

        val actualData   = screen.toString
        val expectedData = Resources.string("term/array-screen-draw-right.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      }
    )
