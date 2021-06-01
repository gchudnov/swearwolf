package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term.{ ParsedReadState, UnknownReadState }
import com.github.gchudnov.swearwolf.CharKeySeq
import zio.test.Assertion.equalTo
import zio.test._

object CharReaderSpec extends DefaultRunnableSpec {

  override def spec: ZSpec[Environment, Failure] =
    suite("CharReader")(
      test(s"empty") {
        val input      = s""
        val inputBytes = input.getBytes

        val expected = UnknownReadState(Seq.empty[Byte])
        val actual   = CharReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"A") {
        val input      = s"A"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CharKeySeq('A'), Seq.empty[Byte])
        val actual   = CharReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"zB") {
        val input      = s"zB"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CharKeySeq('z'), Seq(66.toByte))
        val actual   = CharReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      }
    )

}
