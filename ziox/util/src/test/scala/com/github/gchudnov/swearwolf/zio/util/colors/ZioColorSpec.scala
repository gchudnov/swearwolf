package com.github.gchudnov.swearwolf.zio.util.colors

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.colors.ColorException
import com.github.gchudnov.swearwolf.zio.util.instances.*
import zio.*
import zio.test.Assertion.*
import zio.test.*

object ZioColorSpec extends ZIOSpecDefault:

  override def spec: Spec[TestEnvironment, Any] =
    suite("ZioColor")(
      test("empty") {
        val input = ""

        for actual <- Color.parseZIO(input).exit
        yield (assert(actual)(fails(isSubtype[ColorException](anything))))
      },
      test("wrong format") {
        val input = "#abc"

        for actual <- Color.parseZIO(input).exit
        yield (assert(actual)(fails(isSubtype[ColorException](anything))))
      },
      test("#RRGGBB") {
        val input = "#ff1122"

        val actual   = Color.parseZIO(input)
        val expected = Color(255, 17, 34)

        assertZIO(actual)(equalTo(expected))
      },
      test("RRGGBB") {
        val input = "fe2233"

        val actual   = Color.parseZIO(input)
        val expected = Color(254, 34, 51)

        assertZIO(actual)(equalTo(expected))
      },
      test("red") {
        val input = "red"

        val actual   = Color.parseZIO(input)
        val expected = Color(255, 0, 0)

        assertZIO(actual)(equalTo(expected))
      },
      test("parse name") {
        val input = Seq("white", "black", "red", "green", "yellow", "blue", "rosy-brown", "rosy_brown", "SILVER")

        val actual = ZIO.foreach(input)(name => Color.parseZIO(name))
        val expected = Seq[Color](
          Color.White,
          Color.Black,
          Color.Red,
          Color.Green,
          Color.Yellow,
          Color.Blue,
          Color.RosyBrown,
          Color.RosyBrown,
          Color.Silver
        )

        assertZIO(actual)(equalTo(expected))
      },
      test("parse unknown name") {
        val input = "whot"

        val actual = Color.parseZIO(input).exit

        assertZIO(actual)(fails(isSubtype[ColorException](anything)))
      },
      test("show displays color") {
        val input  = Color.White
        val actual = input.show

        val expected = "#ffffff"

        assert(actual)(equalTo(expected))
      }
    )
