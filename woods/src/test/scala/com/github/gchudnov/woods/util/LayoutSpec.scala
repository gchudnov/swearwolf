package com.github.gchudnov.woods.util

import com.github.gchudnov.swearwolf.util.{ Point, Size }
import com.github.gchudnov.woods.AlignStyle
import zio.test.Assertion._
import zio.test._

object LayoutSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("Layout")(
      test("align left") {
        val sz = Size(48, 2)

        val actual   = Layout.align(sz)("text", AlignStyle.Left)
        val expected = Point(0, 0)

        assert(actual)(equalTo(expected))
      },
      test("align right if there is enough space to fix the text") {
        val sz = Size(48, 2)

        val actual   = Layout.align(sz)("text", AlignStyle.Right)
        val expected = Point(44, 0)

        assert(actual)(equalTo(expected))
      },
      test("align right if there is not enough space to fit the text") {
        val sz = Size(3, 2)

        val actual   = Layout.align(sz)("text", AlignStyle.Right)
        val expected = Point(0, 0)

        assert(actual)(equalTo(expected))
      },
      test("align center") {
        val sz = Size(48, 2)

        val actual   = Layout.align(sz)("text", AlignStyle.Center)
        val expected = Point(22, 0)

        assert(actual)(equalTo(expected))
      }
    )

}
