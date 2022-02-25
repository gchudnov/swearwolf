package com.github.gchudnov.swearwolf.term.internal.readers

import com.github.gchudnov.swearwolf.term.internal.ParsedReadState
import com.github.gchudnov.swearwolf.term.keys.{ KeyModifier }
import com.github.gchudnov.swearwolf.term.keys.{ MouseKeySeq }
import com.github.gchudnov.swearwolf.term.keys.{ MouseAction, MouseButton }
import com.github.gchudnov.swearwolf.util.geometry.{ Point }
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.bytes.Bytes.asBytes
import zio.test.Assertion.*
import zio.test.*

object MouseReaderSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("EscReader")(
      test("1b5b3c303b39323b33334d") {
        val inputBytes = "1b5b3c303b39323b33334d".asBytes

        val expected = ParsedReadState(MouseKeySeq(Point(91, 32), MouseButton.Left, MouseAction.Press), Bytes.empty)
        val actual   = MouseReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("1b5b3c36393b37393b31334d") {
        val inputBytes = "1b5b3c36393b37393b31334d".asBytes

        val expected = ParsedReadState(MouseKeySeq(Point(78, 12), MouseButton.ScrollForward, MouseAction.Press, Set(KeyModifier.Shift)), Bytes.empty)
        val actual   = MouseReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("1b5b3c38353b37373b31364d") {
        val inputBytes = "1b5b3c38353b37373b31364d".asBytes

        val expected = ParsedReadState(MouseKeySeq(Point(76, 15), MouseButton.ScrollForward, MouseAction.Press, Set(KeyModifier.Shift, KeyModifier.Ctrl)), Bytes.empty)
        val actual   = MouseReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      }
    )
