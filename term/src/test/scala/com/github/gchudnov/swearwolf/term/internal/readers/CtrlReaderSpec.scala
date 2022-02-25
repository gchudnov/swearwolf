package com.github.gchudnov.swearwolf.term.internal.readers

import com.github.gchudnov.swearwolf.term.internal.{ ParsedReadState, UnknownReadState }
import com.github.gchudnov.swearwolf.term.keys.{ CharKeySeq, CtrlKeySeq, KeyModifier }
import com.github.gchudnov.swearwolf.term.keys.KeyCode
import zio.test.Assertion.equalTo
import zio.test.*
import com.github.gchudnov.swearwolf.util.bytes.Bytes

object CtrlReaderSpec extends DefaultRunnableSpec:

  private val BackspaceChar = 0x7f.toChar
  private val EscChar       = 0x1b.toChar

  override def spec: ZSpec[Environment, Failure] =
    suite("CtrlReader")(
      test(s"empty") {
        val input      = s""
        val inputBytes = Bytes(input.getBytes)

        val expected = UnknownReadState(Bytes.empty)
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"{ESC}") {
        val input      = s"$EscChar"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Esc, Set.empty[KeyModifier]), Bytes.empty)
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"{ESC}{ESC}") {
        val input      = s"$EscChar$EscChar"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Esc, Set.empty[KeyModifier]), Bytes(EscChar.toByte))
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"{ESC}{ESC}a") {
        val input      = s"$EscChar${EscChar}a"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Esc, Set.empty[KeyModifier]), Bytes(Array(EscChar.toByte, 'a'.toByte)))
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"0x7f == Backspace") {
        val input      = s"$BackspaceChar"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Backspace, Set.empty[KeyModifier]), Bytes.empty)
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"0x08 == Backspace") {
        val input      = s"${0x08.toChar}"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Backspace, Set.empty[KeyModifier]), Bytes.empty)
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"\\r == Enter") {
        val input      = s"\r"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Enter, Set.empty[KeyModifier]), Bytes.empty)
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"\\n == Enter") {
        val input      = s"\n"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Enter, Set.empty[KeyModifier]), Bytes.empty)
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"\\t == Tab") {
        val input      = s"\t"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Tab, Set.empty[KeyModifier]), Bytes.empty)
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test(s"1b31") {
        val input      = s"${0x1b.toChar}${0x31.toChar}"
        val inputBytes = Bytes(input.getBytes)

        val expected = ParsedReadState(CharKeySeq('1', Set(KeyModifier.Alt)), Bytes.empty)
        val actual   = CtrlReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      }
    )
