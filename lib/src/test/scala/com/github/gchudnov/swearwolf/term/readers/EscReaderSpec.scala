package com.github.gchudnov.swearwolf.term.readers

import com.github.gchudnov.swearwolf.term._
import com.github.gchudnov.swearwolf.util.Size
import zio.test.Assertion._
import zio.test._

object EscReaderSpec extends DefaultRunnableSpec {

  private val EscChar = 0x1b.toChar

  override def spec: ZSpec[Environment, Failure] =
    suite("EscReader")(
      test("modifiers can be extracted from 0") {
        val input = 0

        val actual   = EscReader.toModifiers(input)
        val expected = Set.empty[KeyModifier]

        assert(actual)(equalTo(expected))
      },
      test("modifiers can be extracted from 1") {
        val input = 1

        val actual   = EscReader.toModifiers(input)
        val expected = Set.empty[KeyModifier]

        assert(actual)(equalTo(expected))
      },
      test("modifiers can be extracted from 2") {
        val input = 2

        val actual   = EscReader.toModifiers(input)
        val expected = Set[KeyModifier](KeyModifier.Shift)

        assert(actual)(equalTo(expected))
      },
      test("modifiers can be extracted from 3") {
        val input = 3

        val actual   = EscReader.toModifiers(input)
        val expected = Set[KeyModifier](KeyModifier.Alt)

        assert(actual)(equalTo(expected))
      },
      test("modifiers can be extracted from 5") {
        val input = 5

        val actual   = EscReader.toModifiers(input)
        val expected = Set[KeyModifier](KeyModifier.Ctrl)

        assert(actual)(equalTo(expected))
      },
      test("modifiers can be extracted from 4") {
        val input = 4

        val actual   = EscReader.toModifiers(input)
        val expected = Set[KeyModifier](KeyModifier.Shift, KeyModifier.Alt)

        assert(actual)(equalTo(expected))
      },
      test("modifiers can be extracted from 8") {
        val input = 8

        val actual   = EscReader.toModifiers(input)
        val expected = Set[KeyModifier](KeyModifier.Shift, KeyModifier.Alt, KeyModifier.Ctrl)

        assert(actual)(equalTo(expected))
      },
      test("empty") {
        val input      = ""
        val inputBytes = input.getBytes.toSeq

        val expected = (UnknownKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}") {
        val input      = s"${EscChar}"
        val inputBytes = input.getBytes.toSeq

        val expected = (PartialKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[") {
        val input      = s"${EscChar}["
        val inputBytes = input.getBytes.toSeq

        val expected = (PartialKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}{0x9}") {
        val input      = s"${EscChar}\t"
        val inputBytes = input.getBytes.toSeq

        val expected = (UnknownKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[A") {
        val input      = s"${EscChar}[A"
        val inputBytes = input.getBytes.toSeq

        val expected = (CtrlKeySeq(KeyCode.Up, Set.empty[KeyModifier]), Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[8;1") {
        val input      = s"${EscChar}[8;1"
        val inputBytes = input.getBytes.toSeq

        val expected = (PartialKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[8;13") {
        val input      = s"${EscChar}[8;13"
        val inputBytes = input.getBytes.toSeq

        val expected = (PartialKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[8;25;") {
        val input      = s"${EscChar}[8;25;"
        val inputBytes = input.getBytes.toSeq

        val expected = (PartialKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[8;25;1") {
        val input      = s"${EscChar}[8;25;1"
        val inputBytes = input.getBytes.toSeq

        val expected = (PartialKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[8;25;12") {
        val input      = s"${EscChar}[8;25;12"
        val inputBytes = input.getBytes.toSeq

        val expected = (PartialKeySeq, inputBytes): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[8;25;12t") {
        val input      = s"${EscChar}[8;25;12t"
        val inputBytes = input.getBytes.toSeq

        val expected = (SizeKeySeq(Size(width = 12, height = 25)), Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}OP == F1") {
        val input      = s"${EscChar}OP"
        val inputBytes = input.getBytes.toSeq

        val expected = (CtrlKeySeq(KeyCode.F1, Set.empty[KeyModifier]), Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      },
      test("{ESC}[16~ == F5") {
        val input      = s"${EscChar}[16~"
        val inputBytes = input.getBytes.toSeq

        val expected = (CtrlKeySeq(KeyCode.F5, Set.empty[KeyModifier]), Seq.empty[Byte]): (KeySeq, Seq[Byte])
        val actual   = EscReader.read(inputBytes)

        assert(actual)(equalTo(expected))
      }
    )

}
