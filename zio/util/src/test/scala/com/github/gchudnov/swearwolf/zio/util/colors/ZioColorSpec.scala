package com.github.gchudnov.swearwolf.zio.util.colors

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.colors.ColorException
import com.github.gchudnov.swearwolf.zio.util.colors.ZioColor
import zio.*
import zio.test.Assertion.*
import zio.test.*

object ZioColorSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("ZioColor")(
      test("empty") {
        val input = ""

        for actual <- ZioColor.parse(input).exit
        yield (assert(actual)(fails(isSubtype[ColorException](anything))))
      },
      test("wrong format") {
        val input = "#abc"

        for actual <- ZioColor.parse(input).exit
        yield (assert(actual)(fails(isSubtype[ColorException](anything))))
      },
      test("#RRGGBB") {
        val input = "#ff1122"

        val actual   = ZioColor.parse(input)
        val expected = Color(255, 17, 34)

        assertM(actual)(equalTo(expected))
      },
      test("RRGGBB") {
        val input = "fe2233"

        val actual   = ZioColor.parse(input)
        val expected = Color(254, 34, 51)

        assertM(actual)(equalTo(expected))
      },
      test("red") {
        val input = "red"

        val actual   = ZioColor.parse(input)
        val expected = Color(255, 0, 0)

        assertM(actual)(equalTo(expected))
      },
      test("parse name") {
        val input = Seq("white", "black", "red", "green", "yellow", "blue", "rosy-brown", "rosy_brown", "SILVER")

        val actual = ZIO.foreach(input)(name => ZioColor.parse(name))
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

        assertM(actual)(equalTo(expected))
      },
      test("parse unknown name") {
        val input = "whot"

        val actual = ZioColor.parse(input).exit

        assertM(actual)(fails(isSubtype[ColorException](anything)))
      },
      test("show displays color") {
        val input  = Color.White
        val actual = input.show

        val expected = "#ffffff"

        assert(actual)(equalTo(expected))
      }
    )
