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
        val actual = SpanCompiler.compile(span).asHex

        val expected = Bytes.empty.asHex

        assert(actual)(equalTo(expected))
      },
    )

// TODO: how to print bytes in a human readable way?
// TODO: how to implement a custom comparison operator? since now if we compare Bytes, we get incorrect result for the same data