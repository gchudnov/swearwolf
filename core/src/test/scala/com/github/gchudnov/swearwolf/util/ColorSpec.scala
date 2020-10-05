package com.github.gchudnov.swearwolf.util

import zio.test.Assertion._
import zio.test._

object ColorSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("Color")(
      test("empty") {
        val input = ""

        val actual = Color.parse(input)

        assert(actual)(isLeft)
      },
      test("wrong format") {
        val input = "#abc"

        val actual = Color.parse(input)

        assert(actual)(isLeft)
      },
      test("#RRGGBB") {
        val input = "#ff1122"

        val actual   = Color.parse(input).toTry.get
        val expected = Color(255, 17, 34)

        assert(actual)(equalTo(expected))
      },
      test("RRGGBB") {
        val input = "fe2233"

        val actual   = Color.parse(input).toTry.get
        val expected = Color(254, 34, 51)

        assert(actual)(equalTo(expected))
      },
      test("red") {
        val input = "red"

        val actual   = Color.parse(input).toTry.get
        val expected = Color(255, 0, 0)

        assert(actual)(equalTo(expected))
      }
    )
}
