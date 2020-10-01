package com.github.gchudnov.swearwolf.util

import zio.test.Assertion._
import zio.test._
import TextStyle._

object TextStyleSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("TextStyle")(
      test("compose two simple styles") {
        val actual   = Bold | Italic
        val expected = StyleSeqList(Seq(Bold, Italic))

        assert(actual)(equalTo(expected))
      },
      test("compose style and style-seq") {
        val xs       = Bold | Italic
        val x        = Blink
        val actual   = x | xs
        val expected = StyleSeqList(Seq(Blink, Bold, Italic))

        assert(actual)(equalTo(expected))
      },
      test("compose style-seq and style") {
        val xs       = Bold | Italic
        val x        = Blink
        val actual   = xs | x
        val expected = StyleSeqList(Seq(Blink, Bold, Italic))

        assert(actual)(equalTo(expected))
      },
      test("compose style-seq and style-seq") {
        val xs       = Bold | Italic
        val ys       = Blink | Foreground(NamedColor.Black)
        val actual   = xs | ys
        val expected = StyleSeqList(Seq(Bold, Italic, Blink, Foreground(NamedColor.Black)))

        assert(actual)(equalTo(expected))
      }
    )
}
