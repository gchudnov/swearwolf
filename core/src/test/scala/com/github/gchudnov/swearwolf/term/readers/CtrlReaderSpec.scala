package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term.{ ParsedReadState, UnknownReadState }
import com.github.gchudnov.swearwolf.{ CharKeySeq, CtrlKeySeq, KeyCode, KeyModifier }
import zio.test.Assertion.equalTo
import zio.test._

object CtrlReaderSpec extends DefaultRunnableSpec {

  private val BackspaceChar = 0x7f.toChar
  private val EscChar       = 0x1b.toChar

  override def spec: ZSpec[Environment, Failure] =
    suite("CtrlReader")(
      test(s"empty") {
        val input      = s""
        val inputBytes = input.getBytes

        val expected = UnknownReadState(Seq.empty[Byte])
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"{ESC}") {
        val input      = s"$EscChar"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Esc, Set.empty[KeyModifier]), Seq.empty[Byte])
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"{ESC}{ESC}") {
        val input      = s"$EscChar$EscChar"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Esc, Set.empty[KeyModifier]), Seq(EscChar.toByte))
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"{ESC}{ESC}a") {
        val input      = s"$EscChar${EscChar}a"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Esc, Set.empty[KeyModifier]), Seq(EscChar.toByte, 'a'.toByte))
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"0x7f == Backspace") {
        val input      = s"$BackspaceChar"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Backspace, Set.empty[KeyModifier]), Seq.empty[Byte])
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"0x08 == Backspace") {
        val input      = s"${0x08.toChar}"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Backspace, Set.empty[KeyModifier]), Seq.empty[Byte])
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"\\r == Enter") {
        val input      = s"\r"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Enter, Set.empty[KeyModifier]), Seq.empty[Byte])
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"\\n == Enter") {
        val input      = s"\n"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Enter, Set.empty[KeyModifier]), Seq.empty[Byte])
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"\\t == Tab") {
        val input      = s"\t"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CtrlKeySeq(KeyCode.Tab, Set.empty[KeyModifier]), Seq.empty[Byte])
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      },
      test(s"1b31") {
        val input      = s"${0x1b.toChar}${0x31.toChar}"
        val inputBytes = input.getBytes

        val expected = ParsedReadState(CharKeySeq('1', Set(KeyModifier.Alt)), Seq.empty[Byte])
        val actual   = CtrlReader.read(inputBytes.toSeq)

        assert(actual)(equalTo(expected))
      }
    )
}
