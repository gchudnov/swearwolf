package com.github.gchudnov.swearwolf.util

import zio.test.Assertion.*
import zio.test.*

object NamedColorSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("NamedColor")(
      test("parse") {
        val input = Seq("white", "black", "red", "green", "yellow", "blue", "rosy-brown", "rosy_brown", "SILVER")

        val actual = input.map(name => NamedColor.parse(name))
        val expected = Seq[Either[Throwable, Color]](
          Right(NamedColor.White),
          Right(NamedColor.Black),
          Right(NamedColor.Red),
          Right(NamedColor.Green),
          Right(NamedColor.Yellow),
          Right(NamedColor.Blue),
          Right(NamedColor.RosyBrown),
          Right(NamedColor.RosyBrown),
          Right(NamedColor.Silver)
        )

        assert(actual)(equalTo(expected))
      },
      test("parse unknown color") {
        val input  = "whot"
        val actual = NamedColor.parse(input)

        assert(actual)(isLeft)
      }
    )
