package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.Resources
import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.bytes.Bytes.*
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
      },
      test("viewText") {
        val input =
          "1b5b316d424f4c441b5b32326d1b5b33383b323b3137303b303b306d1b5b34383b323b303b3235353b306d4e4f521b5b34396d1b5b33396d4d414c1b5b336d6974616c69631b5b32336d1b5b356d424c494e4b1b5b32356d"
        val bytes = input.asBytes.toArray

        val actual   = ArrayScreen.viewText(bytes)
        val expected = "BOLDNORMALitalicBLINK"

        assert(actual)(equalTo(expected))
      }      
    )
