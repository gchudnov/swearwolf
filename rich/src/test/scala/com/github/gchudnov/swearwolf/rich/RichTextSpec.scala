package com.github.gchudnov.swearwolf.rich

import com.github.gchudnov.swearwolf.rich.RichText
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import zio.test.Assertion.equalTo
import zio.test.Assertion.isLeft
import zio.test.Assertion.isRight
import zio.test.*

object RichTextSpec extends DefaultRunnableSpec:
  given ME: MonadError[Either[Throwable, *]] = EitherMonad

  override def spec: ZSpec[Environment, Failure] =
    suite("RichText")(
      test("empty") {
        val input = ""

        val actual   = RichText.build(RichText(input))
        val expected = Right(StyleSpan(TextStyle.Empty, List.empty[Span]))

        assert(actual)(equalTo(expected))
      },
      test("bold tag without text") {
        val input = "<bold></bold>"

        val actual   = RichText.build(RichText(input))
        val expected = Right(StyleSpan(TextStyle.Empty, List(StyleSpan(TextStyle.Bold, List.empty[Span]))))

        assert(actual)(equalTo(expected))
      },
      test("text without tags") {
        val input = "text"

        val actual   = RichText.build(RichText(input))
        val expected = Right(StyleSpan(TextStyle.Empty, List(TextSpan("text"))))

        assert(actual)(equalTo(expected))
      },
      test("tag with text") {
        val input = "<i>text</i>"

        val actual   = RichText.build(RichText(input))
        val expected = Right(StyleSpan(TextStyle.Empty, List(StyleSpan(TextStyle.Italic, List(TextSpan("text"))))))

        assert(actual)(equalTo(expected))
      },
      test("attribute with single quotes") {
        val input = "<fg='#AABBCC'>text</fg>"

        val actual   = RichText.build(RichText(input))
        val expected = Right(StyleSpan(TextStyle.Empty, List(StyleSpan(TextStyle.Foreground(Color(170, 187, 204)), List(TextSpan("text"))))))

        assert(actual)(equalTo(expected))
      },
      test("attribute with double quotes") {
        val input = "<fg=\"#AABBCC\">text</fg>"

        val actual   = RichText.build(RichText(input))
        val expected = Right(StyleSpan(TextStyle.Empty, List(StyleSpan(TextStyle.Foreground(Color(170, 187, 204)), List(TextSpan("text"))))))

        assert(actual)(equalTo(expected))
      },
      test("multiple attributes") {
        val input = "<fg='#AABBCC'><bg=\"DDEEFF\">text</bg></fg>"

        val actual = RichText.build(RichText(input))
        val expected = Right(
          StyleSpan(
            TextStyle.Empty,
            List(StyleSpan(TextStyle.Foreground(Color(170, 187, 204)), List(StyleSpan(TextStyle.Background(Color(221, 238, 255)), List(TextSpan("text"))))))
          )
        )

        assert(actual)(equalTo(expected))
      },
      test("nested tags") {
        val input = "<i><b>text</b></i>"

        val actual   = RichText.build(RichText(input))
        val expected = Right(StyleSpan(TextStyle.Empty, List(StyleSpan(TextStyle.Italic, List(StyleSpan(TextStyle.Bold, List(TextSpan("text"))))))))

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text in between") {
        val input = "<i>A<b>text</b>B</i>"

        val actual   = RichText.build(RichText(input))
        val expected = Right(StyleSpan(TextStyle.Empty, List(StyleSpan(TextStyle.Italic, List(TextSpan("A"), StyleSpan(TextStyle.Bold, List(TextSpan("text"))), TextSpan("B"))))))

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text and tags in between") {
        val input = "<i>A<b>text</b>B<u>C</u></i>"

        val actual = RichText.build(RichText(input))
        val expected = Right(
          StyleSpan(
            TextStyle.Empty,
            List(
              StyleSpan(
                TextStyle.Italic,
                List(TextSpan("A"), StyleSpan(TextStyle.Bold, List(TextSpan("text"))), TextSpan("B"), StyleSpan(TextStyle.Underline, List(TextSpan("C"))))
              )
            )
          )
        )

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text with spaces") {
        val input = "<i>A B<b>text</b>C </i>"

        val actual = RichText.build(RichText(input))
        val expected =
          Right(StyleSpan(TextStyle.Empty, List(StyleSpan(TextStyle.Italic, List(TextSpan("A B"), StyleSpan(TextStyle.Bold, List(TextSpan("text"))), TextSpan("C "))))))

        assert(actual)(equalTo(expected))
      },
      test("invalid document") {
        val input = "<i>no closing tag"

        val actual = RichText.build(RichText(input))

        assert(actual)(isLeft)
      }
    )
