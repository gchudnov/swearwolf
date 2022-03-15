package com.github.gchudnov.swearwolf.util.styles

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.colors.effects.EitherColor
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle.*
import zio.test.Assertion.*
import zio.test.*

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
        val expected = TextStyleSeq(Seq(Bold, Italic, Blink))

        assert(actual)(equalTo(expected))
      },
      test("compose style-seq and style-seq") {
        val xs       = Bold | Italic
        val ys       = Blink | Foreground(Color.Black)
        val actual   = xs | ys
        val expected = TextStyleSeq(Seq(Bold, Italic, Blink, Foreground(Color.Black)))

        assert(actual)(equalTo(expected))
      },
      test("show displays styles as string") {
        val xs     = Bold | Italic
        val actual = xs.show

        val expected = "bold,italic"

        assert(actual)(equalTo(expected))
      },
      test("contains in composite style on top-level") {
        val xs = TextStyleSeq(Seq(Bold, Italic, TextStyleSeq(Seq(Blink, Foreground(Color.Black)))))

        val actual   = xs.contains(Bold)
        val expected = true

        assert(actual)(equalTo(expected))
      },
      test("contains in composite style on low-level") {
        val xs = TextStyleSeq(Seq(Bold, Italic, TextStyleSeq(Seq(Blink, Foreground(Color.Black)))))

        val actual   = xs.contains(Foreground(Color.Black))
        val expected = true

        assert(actual)(equalTo(expected))
      },
      test("contains in simple style") {
        val xs = Bold

        val actual   = xs.contains(Bold)
        val expected = true

        assert(actual)(equalTo(expected))
      },
      test("contains if style not found") {
        val xs = Bold

        val actual   = xs.contains(Italic)
        val expected = false

        assert(actual)(equalTo(expected))
      }
    )
