package com.github.gchudnov.swearwolf.term.internal.readers

import com.github.gchudnov.swearwolf.term.internal.{ ParsedReadState, UnknownReadState }
import com.github.gchudnov.swearwolf.term.keys.CharKeySeq
import zio.test.Assertion.equalTo
import zio.test.*
import com.github.gchudnov.swearwolf.util.bytes.Bytes

object CharReaderSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("CharReader")(
      test(s"empty") {
        val input      = s""
        val inputBytes = Bytes(input.getBytes)

        val expected = UnknownReadState(Bytes.empty)
        val actual   = CharReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"A") {
        val input      = s"A"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CharKeySeq('A'), Bytes.empty)
        val actual   = CharReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"zB") {
        val input      = s"zB"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CharKeySeq('z'), Bytes(66.toByte))
        val actual   = CharReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      }
    )
