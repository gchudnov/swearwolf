package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.term.ArrayScreen
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.rich.{ Resources}
import com.github.gchudnov.swearwolf.rich.RichText
import zio.test.Assertion.{ equalTo, isLeft, isRight }
import zio.test.*
import com.github.gchudnov.swearwolf.SpanCompiler

// TODO: improve tests
// TODO: NOTE: this class should not be here at all, add these tests to SpanCompilerSpec
// TODO: Compiler should be present only inside of term and not visible outside, outside world should operate on Spans

object RichTextCompiledSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("RichTextCompiled")(
      test("empty") {
        val input = ""
        
        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|||")

        assert(actual)(equalTo(expected))
      },
      test("empty tag") {
        val input = "<bold></bold>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 31 6d 1b 5b 32 32 6d|.[1m.[22m|")

        assert(actual)(equalTo(expected))
      },
      test("text without tags") {
        val input = "text"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed
        
        val expected = Right("|74 65 78 74|text|")

        assert(actual)(equalTo(expected))
      },
      test("tag with text") {
        val input = "<i>text</i>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 33 6d 74 65 78 74 1b 5b 32 33 6d|.[3mtext.[23m|")

        assert(actual)(equalTo(expected))
      },
      test("attribute with single quotes") {
        val input = "<fg='#AABBCC'>text</fg>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 33 38 3b 32 3b 31 37 30 3b 31 38 37 3b 32 30 34 6d 74 65 78 74 1b 5b 33 39 6d|.[38;2;170;187;204mtext.[39m|")

        assert(actual)(equalTo(expected))
      },
      test("attribute with double quotes") {
        val input = "<fg=\"#AABBCC\">text</fg>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 33 38 3b 32 3b 31 37 30 3b 31 38 37 3b 32 30 34 6d 74 65 78 74 1b 5b 33 39 6d|.[38;2;170;187;204mtext.[39m|")

        assert(actual)(equalTo(expected))
      },
      test("multiple attributes") {
        val input = "<fg='#AABBCC'><bg=\"DDEEFF\">text</bg></fg>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 33 38 3b 32 3b 31 37 30 3b 31 38 37 3b 32 30 34 6d 1b 5b 33 38 3b 32 3b 32 32 31 3b 32 33 38 3b 32 35 35 6d 74 65 78 74 1b 5b 33 39 6d 1b 5b 33 39 6d|.[38;2;170;187;204m.[38;2;221;238;255mtext.[39m.[39m|")

        assert(actual)(equalTo(expected))
      },
      test("nested tags") {
        val input = "<i><b>text</b></i>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 33 6d 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 1b 5b 32 33 6d|.[3m.[1mtext.[22m.[23m|")

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text in between") {
        val input = "<i>A<b>text</b>B</i>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 33 6d 41 41 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 41 41 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 42 1b 5b 32 33 6d|.[3mAA.[1mtext.[22mAA.[1mtext.[22mB.[23m|")

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text and tags in between") {
        val input = "<i>A<b>text</b>B<u>C</u></i>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 33 6d 41 41 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 41 41 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 42 41 41 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 41 41 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 42 1b 5b 34 6d 43 1b 5b 32 34 6d 1b 5b 32 33 6d|.[3mAA.[1mtext.[22mAA.[1mtext.[22mBAA.[1mtext.[22mAA.[1mtext.[22mB.[4mC.[24m.[23m|")

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text with spaces") {
        val input = "<i>A B<b>text</b>C </i>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = Right("|1b 5b 33 6d 41 20 42 41 20 42 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 41 20 42 41 20 42 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 43 20 1b 5b 32 33 6d|.[3mA BA B.[1mtext.[22mA BA B.[1mtext.[22mC .[23m|")

        assert(actual)(equalTo(expected))
      },
      test("invalid document") {
        val input = "<i>no closing tag"

        val actual = RichText.make(input)

        assert(actual)(isLeft)
      },
      test("rich-text can be rendered") {
        val input = "<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>"

        val actual = for {
          rich <- RichText.make(input)
          compiled = SpanCompiler.compile(rich.span)
          showed = compiled.show
        } yield showed

        val expected = "1b5b336d4120421b5b316d746578741b5b32326d43201b5b32336d"

        assert(actual)(equalTo(expected))
      }
    )
