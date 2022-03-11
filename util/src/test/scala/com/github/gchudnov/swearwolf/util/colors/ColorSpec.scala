package com.github.gchudnov.swearwolf.util.colors

import com.github.gchudnov.swearwolf.util.colors.EitherColor
import zio.test.Assertion.*
import zio.test.*

object ColorSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("Color")(
      test("empty") {
        val input = ""

        val actual = EitherColor.parse(input)

        assert(actual)(isLeft)
      },
      test("wrong format") {
        val input = "#abc"

        val actual = EitherColor.parse(input)

        assert(actual)(isLeft)
      },
      test("#RRGGBB") {
        val input = "#ff1122"

        val actual   = EitherColor.parse(input).toTry.get
        val expected = EitherColor.make(255, 17, 34)

        assert(actual)(equalTo(expected))
      },
      test("RRGGBB") {
        val input = "fe2233"

        val actual   = EitherColor.parse(input).toTry.get
        val expected = EitherColor.make(254, 34, 51)

        assert(actual)(equalTo(expected))
      },
      test("red") {
        val input = "red"

        val actual   = EitherColor.parse(input).toTry.get
        val expected = EitherColor.make(255, 0, 0)

        assert(actual)(equalTo(expected))
      },
      test("parse name") {
        val input = Seq("white", "black", "red", "green", "yellow", "blue", "rosy-brown", "rosy_brown", "SILVER")

        val actual = input.map(name => EitherColor.parse(name))
        val expected = Seq[Either[Throwable, Color]](
          Right(EitherColor.White),
          Right(EitherColor.Black),
          Right(EitherColor.Red),
          Right(EitherColor.Green),
          Right(EitherColor.Yellow),
          Right(EitherColor.Blue),
          Right(EitherColor.RosyBrown),
          Right(EitherColor.RosyBrown),
          Right(EitherColor.Silver)
        )

        assert(actual)(equalTo(expected))
      },
      test("parse unknown name") {
        val input  = "whot"
        val actual = EitherColor.parse(input)

        assert(actual)(isLeft)
      },
      test("show displays color") {
        val input  = EitherColor.White
        val actual = input.show

        val expected = "#ffffff"

        assert(actual)(equalTo(expected))
      }
    )
