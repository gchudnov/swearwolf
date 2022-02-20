package com.github.gchudnov.swearwolf.util

import zio.test.Assertion.*
import zio.test.*
import TextStyle.*
import com.github.gchudnov.swearwolf.util.colors.NamedColor
import com.github.gchudnov.swearwolf.util.TextStyleSyntax.styleOps

object TextStyleSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("TextStyle")(
      test("compose two simple styles") {
        val actual   = Bold | Italic
        val expected = TextStyleSeq(Seq(Bold, Italic))

        assert(actual)(equalTo(expected))
      },
      test("compose style and style-seq") {
        val xs       = Bold | Italic
        val x        = Blink
        val actual   = x | xs
        val expected = TextStyleSeq(Seq(Blink, Bold, Italic))

        assert(actual)(equalTo(expected))
      },
      test("compose style-seq and style") {
        val xs       = Bold | Italic
        val x        = Blink
        val actual   = xs | x
        val expected = TextStyleSeq(Seq(Blink, Bold, Italic))

        assert(actual)(equalTo(expected))
      },
      test("compose style-seq and style-seq") {
        val xs       = Bold | Italic
        val ys       = Blink | Foreground(NamedColor.Black)
        val actual   = xs | ys
        val expected = TextStyleSeq(Seq(Bold, Italic, Blink, Foreground(NamedColor.Black)))

        assert(actual)(equalTo(expected))
      }
    )
