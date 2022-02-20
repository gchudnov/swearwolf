package com.github.gchudnov.swearwolf.woods.label

import com.github.gchudnov.swearwolf.term.ArrayScreen
import com.github.gchudnov.swearwolf.util.{ Point, Size }
import com.github.gchudnov.swearwolf.woods.{ AlignStyle, Label, Resources }
import com.github.gchudnov.swearwolf.woods.label.impl.LabelDrawer
import com.github.gchudnov.swearwolf.woods.label.LabelSyntax.*
import zio.test.Assertion.*
import zio.test.*

object LabelSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Label")(
      test("background can be filled") {
        val text  = "12"
        val x     = 3
        val width = 7

        val actual   = LabelDrawer.withFilledBackground(width)(x, text)
        val expected = "   12  "

        assert(actual)(equalTo(expected))
      },
      test("draw with align left") {
        val screen = ArrayScreen(Size(32, 32))
        val label  = Label(Size(16, 1), "test data", AlignStyle.Left)

        val actual = screen.put(Point(0, 0), label)

        val actualData   = screen.toString
        val expectedData = Resources.string("label/label-align-left.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("draw with align right") {
        val screen = ArrayScreen(Size(32, 32))
        val label  = Label(Size(16, 1), "test data", AlignStyle.Right)

        val actual = screen.put(Point(0, 0), label)

        val actualData   = screen.toString
        val expectedData = Resources.string("label/label-align-right.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("draw on the bottom edge should draw the label partially") {
        val screen = ArrayScreen(Size(32, 27))
        val label  = Label(Size(16, 4), "this is a very long text that doesnt fit in the provided area entirely", AlignStyle.Left)

        val actual = screen.put(Point(0, 26), label)

        val actualData   = screen.toString
        val expectedData = Resources.string("label/label-draw-bottom.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      },
      test("draw empty") {
        val screen = ArrayScreen(Size(32, 27))
        val label  = Label(Size(0, 0), "", AlignStyle.Left)

        val actual = screen.put(Point(0, 26), label)

        val actualData   = screen.toString
        val expectedData = Resources.string("label/label-empty.txt").toTry.get

        assert(actual)(isRight) &&
        assert(actualData)(equalTo(expectedData))
      }
    )
