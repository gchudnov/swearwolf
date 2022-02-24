package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.bytes.Bytes.asBytes
import zio.test.Assertion.*
import zio.test.*
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.spans.TextSpan

object SpanCompilerSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("SpanCompiler")(
      test("empty span") {
        // ""
        val span   = Span.empty
        val actual = SpanCompiler.compile(span)

        val expected = Bytes.empty

        assert(actual)(equalTo(expected))
      },
      test("span with bold style") {
        // <b></b>
        val span   = StyleSpan(TextStyle.Bold, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 31 6d 1b 5b 32 32 6d|.[1m.[22m|"

        assert(actual)(equalTo(expected))
      },
      test("span with italic style") {
        // <i></i>
        val span   = StyleSpan(TextStyle.Italic, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 6d 1b 5b 32 33 6d|.[3m.[23m|"

        assert(actual)(equalTo(expected))
      },
      test("span with underline style") {
        // <u></u>
        val span   = StyleSpan(TextStyle.Underline, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 34 6d 1b 5b 32 34 6d|.[4m.[24m|"

        assert(actual)(equalTo(expected))
      },
      test("span with blink style") {
        // <blink></blink>
        val span   = StyleSpan(TextStyle.Blink, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 35 6d 1b 5b 32 35 6d|.[5m.[25m|"

        assert(actual)(equalTo(expected))
      },
      test("span with invert style") {
        // <invert></invert>
        val span   = StyleSpan(TextStyle.Invert, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 37 6d 1b 5b 32 37 6d|.[7m.[27m|"

        assert(actual)(equalTo(expected))
      },
      test("span with strikethrough style") {
        // <strikethrough></strikethrough>
        val span   = StyleSpan(TextStyle.Strikethrough, Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 39 6d 1b 5b 32 39 6d|.[9m.[29m|"

        assert(actual)(equalTo(expected))
      },
      test("span with fgcolor") {
        // <fg=0xFF0000></fg>
        val span   = StyleSpan(TextStyle.Foreground(Color(0xff0000)), Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 38 3b 32 3b 32 35 35 3b 30 3b 30 6d 1b 5b 33 39 6d|.[38;2;255;0;0m.[39m|"

        assert(actual)(equalTo(expected))
      },
      test("span with bgcolor") {
        // <bg=0x00FF00></bg>
        val span   = StyleSpan(TextStyle.Background(Color(0, 255, 0)), Seq.empty[Span])
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 38 3b 32 3b 30 3b 32 35 35 3b 30 6d 1b 5b 33 39 6d|.[38;2;0;255;0m.[39m|"

        assert(actual)(equalTo(expected))
      },
      test("span with a text") {
        // "text"
        val span   = TextSpan("text")
        val actual = SpanCompiler.compile(span).show

        val expected = "|74 65 78 74|text|"

        assert(actual)(equalTo(expected))
      },
      test("tag with text") {
        // "<i>text</i>"
        val span   = StyleSpan(TextStyle.Italic, Seq(TextSpan("text")))
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 6d 74 65 78 74 1b 5b 32 33 6d|.[3mtext.[23m|"

        assert(actual)(equalTo(expected))
      },
      test("span with fgcolor and text inside") {
        // <fg=0xFF0000>text</fg>
        val span   = StyleSpan(TextStyle.Foreground(Color(0xff0000)), Seq(TextSpan("text")))
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 38 3b 32 3b 32 35 35 3b 30 3b 30 6d 74 65 78 74 1b 5b 33 39 6d|.[38;2;255;0;0mtext.[39m|"

        assert(actual)(equalTo(expected))
      },
      test("multiple attributes") {
        // <fg color="0xAABBCC"><bg>text</bg></fg>
        val span   = StyleSpan(TextStyle.Foreground(Color(0xaabbcc)), Seq(StyleSpan(TextStyle.Background(Color(0xddeeff)), Seq(TextSpan("text")))))
        val actual = SpanCompiler.compile(span).show

        val expected =
          "|1b 5b 33 38 3b 32 3b 31 37 30 3b 31 38 37 3b 32 30 34 6d 1b 5b 33 38 3b 32 3b 32 32 31 3b 32 33 38 3b 32 35 35 6d 74 65 78 74 1b 5b 33 39 6d 1b 5b 33 39 6d|.[38;2;170;187;204m.[38;2;221;238;255mtext.[39m.[39m|"

        assert(actual)(equalTo(expected))
      },
      test("nested tags") {
        // "<i><b>text</b></i>"
        val span   = StyleSpan(TextStyle.Italic, Seq(StyleSpan(TextStyle.Bold, Seq(TextSpan("text")))))
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 6d 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 1b 5b 32 33 6d|.[3m.[1mtext.[22m.[23m|"

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text in between") {
        // "<i>A<b>text</b>B</i>"
        val span   = StyleSpan(TextStyle.Italic, Seq(TextSpan("A"), StyleSpan(TextStyle.Bold, Seq(TextSpan("text"))), TextSpan("B")))
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 6d 41 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 42 1b 5b 32 33 6d|.[3mA.[1mtext.[22mB.[23m|"

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text and tags in between") {
        // "<i>A<b>text</b>B<u>C</u></i>"
        val span =
          StyleSpan(TextStyle.Italic, Seq(TextSpan("A"), StyleSpan(TextStyle.Bold, Seq(TextSpan("text"))), TextSpan("B"), StyleSpan(TextStyle.Underline, Seq(TextSpan("C")))))
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 6d 41 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 42 1b 5b 34 6d 43 1b 5b 32 34 6d 1b 5b 32 33 6d|.[3mA.[1mtext.[22mB.[4mC.[24m.[23m|"

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text with spaces") {
        // "<i>A B<b>text</b>C </i>"
        val span   = StyleSpan(TextStyle.Italic, Seq(TextSpan("A B"), StyleSpan(TextStyle.Bold, Seq(TextSpan("text"))), TextSpan("C ")))
        val actual = SpanCompiler.compile(span).show

        val expected = "|1b 5b 33 6d 41 20 42 1b 5b 31 6d 74 65 78 74 1b 5b 32 32 6d 43 20 1b 5b 32 33 6d|.[3mA B.[1mtext.[22mC .[23m|"

        assert(actual)(equalTo(expected))
      },
      test("rich-text can be rendered") {
        // "<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>"
        val span = StyleSpan(
          TextStyle.Empty,
          List(
            StyleSpan(TextStyle.Bold, List(TextSpan("BOLD"))),
            StyleSpan(TextStyle.Foreground(Color(170, 0, 0)), List(StyleSpan(TextStyle.Background(Color(0, 255, 0)), List(TextSpan("NOR"))))),
            TextSpan("MAL"),
            StyleSpan(TextStyle.Italic, List(TextSpan("italic"))),
            StyleSpan(TextStyle.Blink, List(TextSpan("BLINK")))
          )
        )
        val actual = SpanCompiler.compile(span).show

        val expected =
          "|1b 5b 31 6d 42 4f 4c 44 1b 5b 32 32 6d 1b 5b 33 38 3b 32 3b 31 37 30 3b 30 3b 30 6d 1b 5b 33 38 3b 32 3b 30 3b 32 35 35 3b 30 6d 4e 4f 52 1b 5b 33 39 6d 1b 5b 33 39 6d 4d 41 4c 1b 5b 33 6d 69 74 61 6c 69 63 1b 5b 32 33 6d 1b 5b 35 6d 42 4c 49 4e 4b 1b 5b 32 35 6d|.[1mBOLD.[22m.[38;2;170;0;0m.[38;2;0;255;0mNOR.[39m.[39mMAL.[3mitalic.[23m.[5mBLINK.[25m|"

        assert(actual)(equalTo(expected))
      }
    )
