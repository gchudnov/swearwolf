package com.github.gchudnov.swearwolf.util

import zio.test.Assertion._
import zio.test._

object BytesSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("Bytes")(
      test("bytes to string") {
        val input = Seq(10.toByte, 23.toByte, 65.toByte)

        val actual   = Bytes.toHexStr(input)
        val expected = "0a1741"

        assert(actual)(equalTo(expected))
      },
      test("string to bytes") {
        val input = "0a1741"

        val actual   = Bytes.fromHexStr(input)
        val expected = Seq(10.toByte, 23.toByte, 65.toByte)

        assert(actual)(equalTo(expected))
      }
    )
}
