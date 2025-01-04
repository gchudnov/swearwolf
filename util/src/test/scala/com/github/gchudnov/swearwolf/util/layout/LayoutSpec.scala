package com.github.gchudnov.swearwolf.util.layout

import com.github.gchudnov.swearwolf.util.geometry.{ Point, Size }
import com.github.gchudnov.swearwolf.util.styles.AlignStyle
import zio.test.Assertion.*
import zio.test.*

object LayoutSpec extends ZIOSpecDefault:
  override def spec: Spec[TestEnvironment, Any] =
    suite("Layout")(
      test("align left") {
        val sz = Size(48, 2)

        val actual   = Layout.align("text", sz.width, AlignStyle.Left).replace(" ", ".")
        val expected = "text............................................"

        assert(actual)(equalTo(expected))
      },
      test("align right if there is enough space to fix the text") {
        val sz = Size(48, 2)

        val actual   = Layout.align("text", sz.width, AlignStyle.Right).replace(" ", ".")
        val expected = "............................................text"

        assert(actual)(equalTo(expected))
      },
      test("align right if there is not enough space to fit the text") {
        val sz = Size(3, 2)

        val actual   = Layout.align("text", sz.width, AlignStyle.Right).replace(" ", ".")
        val expected = "text"

        assert(actual)(equalTo(expected))
      },
      test("align center") {
        val sz = Size(48, 2)

        val actual   = Layout.align("text", sz.width, AlignStyle.Center).replace(" ", ".")
        val expected = "......................text......................"

        assert(actual)(equalTo(expected))
      },
    )
