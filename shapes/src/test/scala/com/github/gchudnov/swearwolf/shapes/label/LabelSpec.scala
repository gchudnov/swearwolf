package com.github.gchudnov.swearwolf.shapes.label

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.shapes.Resources
import com.github.gchudnov.swearwolf.shapes.label.Label
import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import com.github.gchudnov.swearwolf.shapes.label.internal.LabelBuilder
import zio.test.Assertion.*
import zio.test.*

object LabelSpec extends ZIOSpecDefault:
  override def spec: Spec[TestEnvironment, Any] =
    suite("Label")(
      test("draw with align left") {
        val label = Label(Size(16, 1), "test data", AlignStyle.Left)

        val actual   = LabelBuilder.build(label).map(_.show).mkString("\n")
        val expected = Resources.string("label/label-align-left.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("draw with align right") {
        val label = Label(Size(16, 1), "test data", AlignStyle.Right)

        val actual   = LabelBuilder.build(label).map(_.show).mkString("\n")
        val expected = Resources.string("label/label-align-right.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("draw with align center") {
        val label = Label(Size(16, 1), "test data", AlignStyle.Center)

        val actual   = LabelBuilder.build(label).map(_.show).mkString("\n")
        val expected = Resources.string("label/label-align-center.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("draw the label partially") {
        val label = Label(Size(16, 2), "this is a very long text that doesn't fit in the provided area entirely", AlignStyle.Left)

        val actual   = LabelBuilder.build(label).map(_.show).mkString("\n")
        val expected = Resources.string("label/label-draw-partial.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("draw on zero-area") {
        val label = Label(Size(0, 0), "", AlignStyle.Left)

        val actual   = LabelBuilder.build(label).map(_.show).mkString("\n")
        val expected = Resources.string("label/label-empty.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
    )
