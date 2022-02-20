package com.github.gchudnov.swearwolf.util.strings

import com.github.gchudnov.swearwolf.util.Size
import com.github.gchudnov.swearwolf.util.strings.Strings
import com.github.gchudnov.swearwolf.util.strings.Strings.*

import zio.test.Assertion.*
import zio.test.*

object StringsSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Strings")(
      test("empty string can be clipped to the given size") {
        val input = ""

        val sz       = Size(10, 10)
        val actual   = input.clip(sz.width)
        val expected = ""

        assert(actual)(equalTo(expected))
      },
      test("non-empty string can be clipped to the given size if length < width") {
        val input = "str-value"

        val sz       = Size(10, 10)
        val actual   = input.clip(sz.width)
        val expected = "str-value"

        assert(actual)(equalTo(expected))
      },
      test("non-empty string can be clipped to the given size if length == width") {
        val input = "str-value"

        val sz       = Size(input.length, 10)
        val actual   = input.clip(sz.width)
        val expected = "str-value"

        assert(actual)(equalTo(expected))
      },
      test("non-empty string can be clipped to the given size if length > width") {
        val input = "str-value"

        val sz       = Size(3, 10)
        val actual   = input.clip(sz.width)
        val expected = "str"

        assert(actual)(equalTo(expected))
      },
      test("padRight - empty string") {
        val input = ""

        val sz       = Size(10, 10)
        val actual   = input.padRight(sz.width)
        val expected = "          "

        assert(actual)(equalTo(expected))
      },
      test("padRight - non-empty string, length < width") {
        val input = "str-value"

        val sz       = Size(20, 10)
        val actual   = input.padRight(sz.width)
        val expected = "str-value           "

        assert(actual)(equalTo(expected))
      },
      test("padRight - non-empty string, length == width") {
        val input = "str-value"

        val sz       = Size(input.length, 10)
        val actual   = input.padRight(sz.width)
        val expected = "str-value"

        assert(actual)(equalTo(expected))
      },
      test("padRight - non-empty string, length > width") {
        val input = "str-value"

        val sz       = Size(3, 10)
        val actual   = input.padRight(sz.width)
        val expected = "str-value"

        assert(actual)(equalTo(expected))
      },
      test("padLeft - non-empty string, length < width") {
        val input = "str-value"

        val sz       = Size(10, 10)
        val actual   = input.padLeft(sz.width)
        val expected = " str-value"

        assert(actual)(equalTo(expected))
      },
      test("can be sanitized") {
        val input = "\u001bstr-value"

        val actual   = input.sanitize()
        val expected = "?str-value"

        assert(actual)(equalTo(expected))
      },
      test("ellipsis on the right") {
        val input = "this is a very long text that doesn't fit the provided width"
        val width = 16

        val actual   = input.maybeEllipsisRight(width)
        val expected = "this is a ver..."

        assert(actual)(equalTo(expected)) &&
        assert(actual.length)(equalTo(expected.length))
      },
      test("ellipsis on the left") {
        val input = "this is a very long text that doesn't fit the provided width"
        val width = 16

        val actual   = input.maybeEllipsisLeft(width)
        val expected = "...rovided width"

        assert(actual)(equalTo(expected)) &&
        assert(actual.length)(equalTo(expected.length))
      },
      test("wrap") {
        val input = "this is a very long text that doesn't fit the provided width"
        val width = 16

        val actual = input.wrap(width)
        val expected = Seq(
          "this is a very",
          "long text that",
          "doesn't fit the",
          "provided width"
        )

        val maxLen = expected.map(_.length).max

        assert(actual)(equalTo(expected)) &&
        assert(maxLen)(isLessThanEqualTo(width))
      },
      test("wrap words in the middle") {
        val input = "aaa bbb ccc abcdefghiklmnopqrstuvwxyz abcdefghiklmnopqrstuvwxyz"
        val width = 8

        val actual = input.wrap(width)
        val expected = Seq(
          "aaa bbb",
          "ccc abcd",
          "efghiklm",
          "nopqrstu",
          "vwxyz ab",
          "cdefghik",
          "lmnopqrs",
          "tuvwxyz"
        )

        val maxLen = expected.map(_.length).max

        assert(actual)(equalTo(expected)) &&
        assert(maxLen)(isLessThanEqualTo(width))
      },
      test("wrap one very long word") {
        val input = "title/some_very_long_header"
        val width = 20

        val actual = input.wrap(width)
        val expected = Seq(
          "title/some_very_long",
          "_header"
        )

        val maxLen = expected.map(_.length).max

        assert(actual)(equalTo(expected)) &&
        assert(maxLen)(isLessThanEqualTo(width))
      },
      test("wrap one word that fits the width") {
        val input = "abcdefgh"
        val width = 8

        val actual = input.wrap(width)
        val expected = Seq(
          "abcdefgh"
        )

        val maxLen = expected.map(_.length).max

        assert(actual)(equalTo(expected)) &&
        assert(maxLen)(isLessThanEqualTo(width))
      },
      test("wrap several words that fit the width") {
        val input = "abcdefgh ijklmnop"
        val width = 8

        val actual = input.wrap(width)
        val expected = Seq(
          "abcdefgh",
          "ijklmnop"
        )

        val maxLen = expected.map(_.length).max

        assert(actual)(equalTo(expected)) &&
        assert(maxLen)(isLessThanEqualTo(width))
      },
      test("wrap empty word does nothing") {
        val input = ""
        val width = 8

        val actual   = input.wrap(width)
        val expected = Seq.empty[String]

        assert(actual)(equalTo(expected))
      },
      test("force ellipsis left") {
        val text = "this is some text"

        val actual   = text.forceEllipsisLeft()
        val expected = "...s is some text"

        assert(actual)(equalTo(expected))
      },
      test("force ellipsis right") {
        val text = "this is some text"

        val actual   = text.forceEllipsisRight()
        val expected = "this is some t..."

        assert(actual)(equalTo(expected))
      }
    )
