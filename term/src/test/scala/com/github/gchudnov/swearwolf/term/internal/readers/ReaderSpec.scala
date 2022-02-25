package com.github.gchudnov.swearwolf.term.internal.readers

import com.github.gchudnov.swearwolf.term.keys.{ CtrlKeySeq, KeySeq, UnknownKeySeq }
import com.github.gchudnov.swearwolf.term.keys.KeyCode
import com.github.gchudnov.swearwolf.term.*
import zio.test.Assertion.equalTo
import zio.test.*
import com.github.gchudnov.swearwolf.util.bytes.Bytes

object ReaderSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Reader")(
      test(s"empty") {
        val input      = s""
        val inputBytes = Bytes(input.getBytes)

        val expected = (Vector.empty[KeySeq], Bytes.empty): (Vector[KeySeq], Bytes)
        val actual   = Reader.consume(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"1b1b") {
        val input      = s"${0x1b.toByte.toChar}${0x1b.toByte.toChar}"
        val inputBytes = Bytes(input.getBytes)

        val expected = (Vector(CtrlKeySeq(KeyCode.Esc), CtrlKeySeq(KeyCode.Esc)), Bytes.empty): (Vector[KeySeq], Bytes)
        val actual   = Reader.consume(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"1bd099") {
        val inputBytes = Bytes(Array(0x1b.toByte, 0xd0.toByte, 0x99.toByte))

        val expected = (Vector(UnknownKeySeq(Bytes(Array[Byte](27, -48, -103)))), Bytes.empty): (Vector[KeySeq], Bytes)
        val actual   = Reader.consume(inputBytes)

        assert(actual)(equalTo(expected))
      }
    )
