package com.github.gchudnov.swearwolf.util.numerics

import com.github.gchudnov.swearwolf.util.numerics.Numerics
import com.github.gchudnov.swearwolf.util.numerics.Numerics.*

import zio.test.Assertion.*
import zio.test.*

object NumericsSpec extends ZIOSpecDefault:
  override def spec: Spec[TestEnvironment, Any] =
    suite("Numerics")(
      test("clamp") {
        val min = 0.0
        val max = 4.0

        val table = List(
          -1.0 -> 0.0,
          0.0  -> 0.0,
          1.0  -> 1.0,
          1.5  -> 1.5,
          2.0  -> 2.0,
          3.0  -> 3.0,
          4.0  -> 4.0,
          5.0  -> 4.0
        )

        val input    = table.map(_._1)
        val expected = table.map(_._2)

        val actual = input.map(it => it.clamp(min, max))

        assert(actual)(equalTo(expected))
      },
      test("scale seq double >= 0") {
        val points    = Seq(0.0, 50.0, 100.0, 200.0)
        val ceilValue = 100.0
        val maxValue  = Option.empty[Double]

        val actual   = points.scaleSeq(ceilValue, maxValue)
        val expected = Seq(0.0, 25.0, 50.0, 100.0)

        assert(actual)(equalTo(expected))
      },
      test("scale seq double <= 0") {
        val points    = Seq(0.0, -50.0, -100.0, -200.0)
        val ceilValue = 100.0
        val maxValue  = Option.empty[Double]

        val actual   = points.scaleSeq(ceilValue, maxValue)
        val expected = Seq(0.0, -25.0, -50.0, -100.0)

        assert(actual)(equalTo(expected))
      },
      test("scale seq double <= 0 ; >= 0") {
        val points    = Seq(-200.0, -100.0, 100.0, 200.0)
        val ceilValue = 100.0
        val maxValue  = Option.empty[Double]

        val actual   = points.scaleSeq(ceilValue, maxValue)
        val expected = Seq(-100.0, -50.0, 50.0, 100.0)

        assert(actual)(equalTo(expected))
      },
      test("scale seq int >= 0") {
        val points    = Seq(0, 50, 100, 200)
        val ceilValue = 100
        val maxValue  = Option.empty[Int]

        val actual   = points.scaleSeq(ceilValue, maxValue)
        val expected = Seq(0, 25, 50, 100)

        assert(actual)(equalTo(expected))
      },
      test("scale seq int >= 0 with max value") {
        val points    = Seq(0, 50, 100, 200)
        val ceilValue = 100
        val maxValue  = Option(200)

        val actual   = points.scaleSeq(ceilValue, maxValue)
        val expected = Seq(0, 25, 50, 100)

        assert(actual)(equalTo(expected))
      },
      test("scale seq of 0") {
        val points    = Seq(0, 0)
        val ceilValue = 100
        val maxValue  = Option.empty[Int]

        val actual   = points.scaleSeq(ceilValue, maxValue)
        val expected = Seq(0, 0)

        assert(actual)(equalTo(expected))
      }
    )
