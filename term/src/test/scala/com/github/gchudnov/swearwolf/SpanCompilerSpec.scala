package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.bytes.Bytes.asBytes
import zio.test.Assertion.*
import zio.test.*
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.colors.Color

object SpanCompilerSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("SpanCompiler")(
      test("empty span") {
        val span = Span.empty
        val actual = SpanCompiler.compile(span)

        val expected = Bytes.empty

        assert(actual)(equalTo(expected))
      },
      test("span with bold style") {
        val span = StyleSpan(TextStyle.Bold, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 31 6d 1b 5b 32 32 6d|.[1m.[22m|"

        assert(actual)(equalTo(expected))
      },
      test("span with italic style") {
        val span = StyleSpan(TextStyle.Italic, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 6d 1b 5b 32 33 6d|.[3m.[23m|"

        assert(actual)(equalTo(expected))
      },
      test("span with underline style") {
        val span = StyleSpan(TextStyle.Underline, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 34 6d 1b 5b 32 34 6d|.[4m.[24m|"

        assert(actual)(equalTo(expected))
      },
      test("span with blink style") {
        val span = StyleSpan(TextStyle.Blink, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 35 6d 1b 5b 32 35 6d|.[5m.[25m|"

        assert(actual)(equalTo(expected))
      },
      test("span with invert style") {
        val span = StyleSpan(TextStyle.Invert, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 37 6d 1b 5b 32 37 6d|.[7m.[27m|"

        assert(actual)(equalTo(expected))
      },
      test("span with strikethrough style") {
        val span = StyleSpan(TextStyle.Strikethrough, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 39 6d 1b 5b 32 39 6d|.[9m.[29m|"

        assert(actual)(equalTo(expected))
      },
      test("span with fgcolor") {
        val span = StyleSpan(TextStyle.Foreground(Color(0xFF0000)), Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 38 3b 32 3b 32 35 35 3b 30 3b 30 6d 1b 5b 33 39 6d|.[38;2;255;0;0m.[39m|"

        assert(actual)(equalTo(expected))
      },
      test("span with bgcolor") {
        val span = StyleSpan(TextStyle.Background(Color(0, 255, 0)), Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 38 3b 32 3b 30 3b 32 35 35 3b 30 6d 1b 5b 33 39 6d|.[38;2;0;255;0m.[39m|"

        assert(actual)(equalTo(expected))
      }      
    )
