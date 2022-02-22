package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.colors.Color
import com.github.gchudnov.swearwolf.util.bytes.Bytes
import com.github.gchudnov.swearwolf.util.bytes.Bytes.asBytes
import zio.test.Assertion.*
import zio.test.*

object EscSeqSpec extends DefaultRunnableSpec:

  override def spec: ZSpec[Environment, Failure] =
    suite("EscSeq")(
      test("foreground color") {
        val input = EscSeq.foreground(Color(255, 0, 0))

        // ESC[ 38;2;⟨r⟩;⟨g⟩;⟨b⟩ m Select RGB foreground color

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b33383b323b3235353b303b306d"

        assert(actual)(equalTo(expected))
      },
      test("background color") {
        val input = EscSeq.background(Color(255, 0, 0))

        // ESC[ 48;2;⟨r⟩;⟨g⟩;⟨b⟩ m Select RGB background color

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b34383b323b3235353b303b306d"

        assert(actual)(equalTo(expected))
      },
      test("reset foreground color") {
        val input = EscSeq.resetForeground

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b33396d"

        assert(actual)(equalTo(expected))
      },
      test("reset background color") {
        val input = EscSeq.resetBackground

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b34396d"

        assert(actual)(equalTo(expected))
      },
      test("screen erase") {
        val input = EscSeq.erase

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b324a"

        assert(actual)(equalTo(expected))
      },
      test("bold") {
        val input = EscSeq.bold

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b316d"

        assert(actual)(equalTo(expected))
      },
      test("reset bold") {
        val input = EscSeq.resetBold

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b32326d"

        assert(actual)(equalTo(expected))
      },
      test("underline") {
        val input = EscSeq.underline

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b346d"

        assert(actual)(equalTo(expected))
      },
      test("reset underline") {
        val input = EscSeq.resetUnderline

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b32346d"

        assert(actual)(equalTo(expected))
      },
      test("blink") {
        val input = EscSeq.blink

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b356d"

        assert(actual)(equalTo(expected))
      },
      test("reset blink") {
        val input = EscSeq.resetBlink

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b32356d"

        assert(actual)(equalTo(expected))
      },
      test("invert") {
        val input = EscSeq.invert

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b376d"

        assert(actual)(equalTo(expected))
      },
      test("reset invert") {
        val input = EscSeq.resetInvert

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b32376d"

        assert(actual)(equalTo(expected))
      },
      test("reset all") {
        val input = EscSeq.reset

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b306d"

        assert(actual)(equalTo(expected))
      },
      test("italic") {
        val input = EscSeq.italic

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b336d"

        assert(actual)(equalTo(expected))
      },
      test("reset italic") {
        val input = EscSeq.resetItalic

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b32336d"

        assert(actual)(equalTo(expected))
      },
      test("strikethrough") {
        val input = EscSeq.strikethrough

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b396d"

        assert(actual)(equalTo(expected))
      },
      test("reset strikethrough") {
        val input = EscSeq.resetStrikethrough

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b32396d"

        assert(actual)(equalTo(expected))
      },
      test("cursor show") {
        val input = EscSeq.cursorShow

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b3f32353b68"

        assert(actual)(equalTo(expected))
      },
      test("cursor hide") {
        val input = EscSeq.cursorHide

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b3f32353b6c"

        assert(actual)(equalTo(expected))
      },
      test("cursor save") {
        val input = EscSeq.cursorSave

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b37"

        assert(actual)(equalTo(expected))
      },
      test("cursor restore") {
        val input = EscSeq.cursorRestore

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b38"

        assert(actual)(equalTo(expected))
      },
      test("cursor up") {
        val input = EscSeq.cursorUp(1)

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b3141"

        assert(actual)(equalTo(expected))
      },
      test("cursor down") {
        val input = EscSeq.cursorDown(1)

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b3142"

        assert(actual)(equalTo(expected))
      },
      test("cursor right") {
        val input = EscSeq.cursorRight(1)

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b3143"

        assert(actual)(equalTo(expected))
      },
      test("cursor left") {
        val input = EscSeq.cursorLeft(1)

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b3144"

        assert(actual)(equalTo(expected))
      },
      test("alt buffer") {
        val input = EscSeq.altBuffer

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b3f34373b68"

        assert(actual)(equalTo(expected))
      },
      test("normal buffer") {
        val input = EscSeq.normalBuffer

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b3f34373b6c"

        assert(actual)(equalTo(expected))
      },
      test("device status") {
        val input = EscSeq.status

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b356e"

        assert(actual)(equalTo(expected))
      },
      test("text area size") {
        val input = EscSeq.textAreaSize

        val actual   = Bytes(input.bytes.toSeq).asHex
        val expected = "1b5b313874"

        assert(actual)(equalTo(expected))
      },
      test("bytes parsed into a string") {
        val input =
          "1b5b316d424f4c441b5b32326d1b5b33383b323b3137303b303b306d1b5b34383b323b303b3235353b306d4e4f521b5b34396d1b5b33396d4d414c1b5b336d6974616c69631b5b32336d1b5b356d424c494e4b1b5b32356d"
        val bytes = input.asBytes.value.toArray

        val actual   = EscSeq.textFromBytes(bytes)
        val expected = "BOLDNORMALitalicBLINK"

        assert(actual)(equalTo(expected))
      }
    )
