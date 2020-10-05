package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.{CtrlKeySeq, KeyCode, KeySeq, UnfamiliarKeySeq}
import com.github.gchudnov.swearwolf.term._
import zio.test.Assertion.equalTo
import zio.test._

object ReaderSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("Reader")(
      test(s"empty") {
        val input      = s""
        val inputBytes = input.getBytes

        val expected = (Vector.empty[KeySeq], Seq.empty[Byte]): (Vector[KeySeq], Seq[Byte])
        val actual   = Reader.consume(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"1b1b") {
        val input      = s"${0x1b.toByte.toChar}${0x1b.toByte.toChar}"
        val inputBytes = input.getBytes

        val expected = (Vector(CtrlKeySeq(KeyCode.Esc), CtrlKeySeq(KeyCode.Esc)), Seq.empty[Byte]): (Vector[KeySeq], Seq[Byte])
        val actual   = Reader.consume(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"1bd099") {
        val inputBytes = Array(0x1b.toByte, 0xd0.toByte, 0x99.toByte)

        val expected = (Vector(UnfamiliarKeySeq(Seq(27, -48, -103))), Seq.empty[Byte]): (Vector[KeySeq], Seq[Byte])
        val actual   = Reader.consume(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      }
    )
}
