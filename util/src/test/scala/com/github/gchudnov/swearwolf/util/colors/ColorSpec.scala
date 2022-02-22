package com.github.gchudnov.swearwolf.util.colors

import zio.test.Assertion.*
import zio.test.*

object ColorSpec extends DefaultRunnableSpec:
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
      },
      test("parse name") {
        val input = Seq("white", "black", "red", "green", "yellow", "blue", "rosy-brown", "rosy_brown", "SILVER")

        val actual = input.map(name => Color.parse(name))
        val expected = Seq[Either[Throwable, Color]](
          Right(Color.White),
          Right(Color.Black),
          Right(Color.Red),
          Right(Color.Green),
          Right(Color.Yellow),
          Right(Color.Blue),
          Right(Color.RosyBrown),
          Right(Color.RosyBrown),
          Right(Color.Silver)
        )

        assert(actual)(equalTo(expected))
      },
      test("parse unknown name") {
        val input  = "whot"
        val actual = Color.parse(input)

        assert(actual)(isLeft)
      }      
    )
