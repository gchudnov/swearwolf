package com.github.gchudnov.swearwolf

import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.bytes.Bytes.asBytes
import zio.test.Assertion.*
import zio.test.*

object SpanCompilerSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("SpanCompiler")(
      test("empty span") {
        val span = Span.empty
        val actual = SpanCompiler.compile(span)

        val expected = Bytes.empty

        assert(actual)(equalTo(expected))
      },
    )
