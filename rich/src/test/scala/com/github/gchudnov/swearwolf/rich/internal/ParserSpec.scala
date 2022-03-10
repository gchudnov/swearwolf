package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.internal.Parser
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.util.func.MonadError
import zio.test.Assertion.anything
import zio.test.Assertion.equalTo
import zio.test.Assertion.isLeft
import zio.test.Assertion.isSubtype
import zio.test.*

object ParserSpec extends DefaultRunnableSpec:
  given ME: MonadError[Either[Throwable, *]] = EitherMonad

  override def spec: ZSpec[Environment, Failure] =
    suite("Parser")(
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
      test("input has newlines") {
        val input    = "a\nb\nc"
        val expected = Right(Seq(TextElement("a"), NewLineElement, TextElement("b"), NewLineElement, TextElement("c")))
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
      test("input is a tag with nested tags") {
        val input    = "<tag><inner>text</inner></tag>"
        val expected = Right(Seq(TagElement("tag", None, List(TagElement("inner", None, List(TextElement("text")))))))
        val actual   = Parser.parse(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a a sequence of tags") {
        val input = "<tag1='value1'><inner1>text1</inner1></tag1>\n<tag2><inner2>text2</inner2></tag2>"
        val expected = Right(
          Seq(
            TagElement("tag1", Some("value1"), List(TagElement("inner1", None, List(TextElement("text1"))))),
            NewLineElement,
            TagElement("tag2", None, List(TagElement("inner2", None, List(TextElement("text2")))))
          )
        )
        val actual = Parser.parse(input)

        assert(actual)(equalTo(expected))
      },
      test("input with only a closing tag generates an error") {
        val input  = "</tag>"
        val actual = Parser.parse(input)

        assert(actual)(isLeft(isSubtype[ParserException](anything)))
      },
      test("input with only an opening tag generates an error") {
        val input  = "<tag>"
        val actual = Parser.parse(input)

        assert(actual)(isLeft(isSubtype[ParserException](anything)))
      },
      test("input with mismatching opening and closing tags generates an error") {
        val input  = "<tag>text</inner>"
        val actual = Parser.parse(input)

        assert(actual)(isLeft(isSubtype[ParserException](anything)))
      },
      test("input with one opening tag and two closing tags generates an error") {
        val input  = "<tag>text</tag></inner>"
        val actual = Parser.parse(input)

        assert(actual)(isLeft(isSubtype[ParserException](anything)))
      }
    )
