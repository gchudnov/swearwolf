package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.internal.Builder
import com.github.gchudnov.swearwolf.rich.internal.Element
import com.github.gchudnov.swearwolf.rich.internal.TagElement
import com.github.gchudnov.swearwolf.rich.internal.TextElement
import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import com.github.gchudnov.swearwolf.util.func.MonadError
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.spans.StyleSpan
import com.github.gchudnov.swearwolf.util.spans.TextSpan
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import zio.test.Assertion.anything
import zio.test.Assertion.equalTo
import zio.test.Assertion.isLeft
import zio.test.Assertion.isSubtype
import zio.test.*

object BuilderSpec extends ZIOSpecDefault:
  given ME: MonadError[Either[Throwable, *]] = EitherMonad

  override def spec: Spec[TestEnvironment, Any] =
    suite("Builder")(
      test("input is empty") {
        val input    = Seq.empty[Element]
        val actual   = Builder.build(input)
        val expected = Right(Span.empty)

        assert(actual)(equalTo(expected))
      },
      test("input is a text ") {
        val input    = Seq(TextElement("a"))
        val actual   = Builder.build(input)
        val expected = Right(wrapSpan(TextSpan("a")))

        assert(actual)(equalTo(expected))
      },
      test("input is a text with style") {
        val input    = Seq(TagElement("fg", "red"))
        val actual   = Builder.build(input)
        val expected = Right(wrapSpan(StyleSpan(TextStyle.Foreground(Color.Red), Seq.empty[Span])))

        assert(actual)(equalTo(expected))
      },
      test("input is several elements") {
        val input  = Seq(TagElement("fg", "red"), TextElement("a"), TagElement("bg", "blue"), TextElement("b"))
        val actual = Builder.build(input)
        val expected = Right(
          StyleSpan(
            TextStyle.empty,
            Seq(
              StyleSpan(TextStyle.Foreground(Color.Red), Seq.empty[Span]),
              TextSpan("a"),
              StyleSpan(TextStyle.Background(Color.Blue), Seq.empty[Span]),
              TextSpan("b")
            )
          )
        )

        assert(actual)(equalTo(expected))
      },
      test("input is several nested elements") {
        val input  = Seq(TagElement("b", None, Seq(TagElement("bg", Some("blue"), Seq(TextElement("b"))))))
        val actual = Builder.build(input)
        val expected = Right(
          StyleSpan(
            TextStyle.empty,
            Seq(
              StyleSpan(TextStyle.Bold, Seq(StyleSpan(TextStyle.Background(Color.Blue), Seq(TextSpan("b")))))
            )
          )
        )

        assert(actual)(equalTo(expected))
      }
    )

  private def wrapSpan(s: Span): Span =
    StyleSpan(TextStyle.empty, Seq(s))
