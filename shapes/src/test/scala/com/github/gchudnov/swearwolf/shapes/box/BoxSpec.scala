package com.github.gchudnov.swearwolf.shapes.box

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.shapes.box.{ Box, BoxStyle }
import com.github.gchudnov.swearwolf.shapes.Resources
import com.github.gchudnov.swearwolf.shapes.box.internal.BoxBuilder
import zio.test.Assertion.*
import zio.test.*

object BoxSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Box")(
      test("single border") {
        val box    = Box(Size(20, 10), BoxStyle.SingleBorder)

        val actual = BoxBuilder.build(box).map(_.show).mkString("\n")
        val expected = Resources.string("box/box-single-border.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("double border") {
        val box    = Box(Size(20, 10), BoxStyle.DoubleBorder)

        val actual = BoxBuilder.build(box).map(_.show).mkString("\n")
        val expected = Resources.string("box/box-double-border.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("bold border") {
        val box    = Box(Size(20, 10), BoxStyle.BoldBorder)

        val actual = BoxBuilder.build(box).map(_.show).mkString("\n")
        val expected = Resources.string("box/box-bold-border.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("size is one") {
        val box    = Box(Size(1, 1), BoxStyle.BoldBorder)

        val actual = BoxBuilder.build(box).map(_.show).mkString("\n")
        val expected = Resources.string("box/box-too-small.txt").toTry.get

        assert(actual)(equalTo(expected))
      },
      test("size is zero") {
        val box    = Box(Size(0, 0), BoxStyle.BoldBorder)

        val actual = BoxBuilder.build(box).map(_.show).mkString("\n")
        val expected = Resources.string("box/box-empty.txt").toTry.get

        assert(actual)(equalTo(expected))
      }
    )
