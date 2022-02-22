package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.colors.Color
import RichTextStyler.*
import zio.test.Assertion.equalTo
import zio.test.*

object RichTextCompilerSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("RichTextCompiler")(
      test("empty") {
        val input = RichBoxStyle(Seq.empty[RichStyle])

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = ""

        assert(actual)(equalTo(expected))
      },
      test("bold") {
        val input = RichBoxStyle(Seq(RichBoldStyle(Seq.empty[RichStyle])))

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = "1b5b316d1b5b32326d"

        assert(actual)(equalTo(expected))
      },
      test("italic") {
        val input = RichBoxStyle(Seq(RichItalicStyle(Seq.empty[RichStyle])))

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = "1b5b336d1b5b32336d"

        assert(actual)(equalTo(expected))
      },
      test("underline") {
        val input = RichBoxStyle(Seq(RichUnderlineStyle(Seq.empty[RichStyle])))

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = "1b5b346d1b5b32346d"

        assert(actual)(equalTo(expected))
      },
      test("blink") {
        val input = RichBoxStyle(Seq(RichBlinkStyle(Seq.empty[RichStyle])))

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = "1b5b356d1b5b32356d"

        assert(actual)(equalTo(expected))
      },
      test("invert") {
        val input = RichBoxStyle(Seq(RichInvertStyle(Seq.empty[RichStyle])))

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = "1b5b376d1b5b32376d"

        assert(actual)(equalTo(expected))
      },
      test("strikethrough") {
        val input = RichBoxStyle(Seq(RichStrikethroughStyle(Seq.empty[RichStyle])))

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = "1b5b396d1b5b32396d"

        assert(actual)(equalTo(expected))
      },
      test("fgcolor") {
        val input = RichBoxStyle(Seq(RichForegroundStyle(Color(255, 0, 0), Seq.empty[RichStyle])))

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = "1b5b33383b323b3235353b303b306d1b5b33396d"

        assert(actual)(equalTo(expected))
      },
      test("bgcolor") {
        val input = RichBoxStyle(Seq(RichBackgroundStyle(Color(0, 255, 0), Seq.empty[RichStyle])))

        val actual   = Bytes(RichTextCompiler.compile(input).toSeq).asHex
        val expected = "1b5b34383b323b303b3235353b306d1b5b34396d"

        assert(actual)(equalTo(expected))
      }
    )
