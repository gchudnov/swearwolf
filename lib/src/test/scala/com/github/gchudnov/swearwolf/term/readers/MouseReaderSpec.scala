package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term._
import com.github.gchudnov.swearwolf.util.{ Bytes, Point }
import zio.test.Assertion._
import zio.test._

object MouseReaderSpec extends DefaultRunnableSpec {

  override def spec: ZSpec[Environment, Failure] =
    suite("EscReader")(
      test("1b5b3c303b39323b33334d") {
        val inputBytes = Bytes.fromHexStr("1b5b3c303b39323b33334d")

        val expected = (MouseKeySeq(Point(91, 32), MouseButton.Left, MouseAction.Press), Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = MouseReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("1b5b3c36393b37393b31334d") {
        val inputBytes = Bytes.fromHexStr("1b5b3c36393b37393b31334d")

        val expected = (MouseKeySeq(Point(78, 12), MouseButton.ScrollForward, MouseAction.Press, Set(KeyModifier.Shift)), Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = MouseReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("1b5b3c38353b37373b31364d") {
        val inputBytes = Bytes.fromHexStr("1b5b3c38353b37373b31364d")

        val expected = (MouseKeySeq(Point(76, 15), MouseButton.ScrollForward, MouseAction.Press, Set(KeyModifier.Shift, KeyModifier.Ctrl)), Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = MouseReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      }
    )

}
