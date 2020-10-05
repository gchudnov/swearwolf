package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.{CharKeySeq, KeySeq, UnknownKeySeq}
import zio.test.Assertion.equalTo
import zio.test._

object CharReaderSpec extends DefaultRunnableSpec {

  override def spec: ZSpec[Environment, Failure] =
    suite("CharReader")(
      test(s"empty") {
        val input      = s""
        val inputBytes = input.getBytes

        val expected = (UnknownKeySeq, Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = CharReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"A") {
        val input      = s"A"
        val inputBytes = input.getBytes

        val expected = (CharKeySeq('A'), Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = CharReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"zB") {
        val input      = s"zB"
        val inputBytes = input.getBytes

        val expected = (CharKeySeq('z'), Seq(66.toByte)): (KeySeq, Seq[Byte])
        val actual   = CharReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      }
    )

}
