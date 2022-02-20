package com.github.gchudnov.swearwolf.woods.box

import com.github.gchudnov.swearwolf.term.ArrayScreen
import com.github.gchudnov.swearwolf.util.{ Point, Size }
import com.github.gchudnov.swearwolf.woods.{ Box, BoxStyle, Resources }
import com.github.gchudnov.swearwolf.woods.box.BoxSyntax.*
import com.github.gchudnov.swearwolf.woods.*
import zio.test.Assertion.*
import zio.test.*

object BoxSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Box")(
      test("single border") {
        val screen = ArrayScreen(Size(32, 32))
        val box    = Box(Size(20, 10), BoxStyle.SingleBorder)

        val actual = screen.put(Point(0, 0), box)

        val actualData   = screen.toString
        val expectedData = Resources.string("box/box-single-border.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("double border") {
        val screen = ArrayScreen(Size(32, 32))
        val box    = Box(Size(20, 10), BoxStyle.DoubleBorder)

        val actual = screen.put(Point(0, 0), box)

        val actualData   = screen.toString
        val expectedData = Resources.string("box/box-double-border.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("bold border") {
        val screen = ArrayScreen(Size(32, 32))
        val box    = Box(Size(20, 10), BoxStyle.BoldBorder)

        val actual = screen.put(Point(0, 0), box)

        val actualData   = screen.toString
        val expectedData = Resources.string("box/box-bold-border.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("size is too small") {
        val screen = ArrayScreen(Size(32, 32))
        val box    = Box(Size(1, 1), BoxStyle.BoldBorder)

        val actual = screen.put(Point(0, 0), box)

        val actualData   = screen.toString
        val expectedData = Resources.string("box/box-too-small.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("size is empty") {
        val screen = ArrayScreen(Size(32, 32))
        val box    = Box(Size(0, 0), BoxStyle.BoldBorder)

        val actual = screen.put(Point(0, 0), box)

        val actualData   = screen.toString
        val expectedData = Resources.string("box/box-empty.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      }
    )
