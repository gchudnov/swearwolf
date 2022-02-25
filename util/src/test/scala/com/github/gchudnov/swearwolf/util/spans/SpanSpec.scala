package com.github.gchudnov.swearwolf.util.spans

import zio.test.Assertion.*
import zio.test.*
import com.github.gchudnov.swearwolf.util.spans.*
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.util.styles.TextStyle.*
import com.github.gchudnov.swearwolf.util.colors.Color

object SpanSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Span")(
      test("show is called on a nested span") {
        val span     = StyleSpan(Bold | Italic, Seq(TextSpan("nested"), ByteSpan(Array[Byte](1, 2, 3))))
        val actual   = span.show
        val expected = "[bold,italic](nested|01 02 03|...|)"

        assert(actual)(equalTo(expected))
      }
    )
