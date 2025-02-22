package com.github.gchudnov.swearwolf.util.bytes

import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.bytes.Bytes.asBytes
import com.github.gchudnov.swearwolf.util.func.EitherMonad
import zio.test.Assertion.*
import zio.test.*

object BytesSpec extends ZIOSpecDefault:
  override def spec: Spec[TestEnvironment, Any] =
    suite("Bytes")(
      test("bytes to string") {
        val input = Array(10.toByte, 23.toByte, 65.toByte)

        val actual   = Bytes(input).asHex
        val expected = "0a1741"

        assert(actual)(equalTo(expected))
      },
      test("string to bytes") {
        val input = "0a1741"

        val actual   = input.asBytes
        val expected = Bytes(Array(10.toByte, 23.toByte, 65.toByte))

        assert(actual)(equalTo(Right(expected)))
      },
      test("string to bytes with non-hex characters") {
        val input = "afdsgbvvjytut"

        val actual = input.asBytes

        assert(actual)(isLeft)
      },
      test("show bytes") {
        val input = Array(10.toByte, 23.toByte, 65.toByte)

        val actual = Bytes(input).show

        val expected = "|0a 17 41|..A|"

        assert(actual)(equalTo(expected))
      },
    )
