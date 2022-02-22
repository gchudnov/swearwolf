package com.github.gchudnov.swearwolf.rich.internal.parser

import com.github.gchudnov.swearwolf.rich.internal.parser.Parser

import zio.test.Assertion.{ equalTo, isLeft }
import zio.test.*

object ParserSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("ParserSpec")(
      test("input is empty") {
        val input    = ""
        val expected = Right(Seq.empty[Element])
        val actual   = Parser.parse(input)

        assert(actual)(equalTo(expected))
      },
      test("input is whitespace") {
        val input    = "   "
        val expected = Right(Seq(TextElement("   ")))
        val actual   = Parser.parse(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a single word") {
        val input    = "word"
        val expected = Right(Seq(TextElement("word")))
        val actual   = Parser.parse(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a tag with text") {
        val input    = "<tag>text</tag>"
        val expected = Right(Seq(TagElement("tag", None, List(TextElement("text")))))
        val actual   = Parser.parse(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a tag with text and attribute") {
        val input    = "<tag=\"value1\">text</tag>"
        val expected = Right(Seq(TagElement("tag", Some("value1"), List(TextElement("text")))))
        val actual   = Parser.parse(input)

        assert(actual)(equalTo(expected))
      },
    )

    // // Re, Ae, T1, T2, Be, T3, Bx, T4, Ax, Ce, T5, Cx, Rx
