package com.github.gchudnov.swearwolf.woods.text

import com.github.gchudnov.swearwolf.term.ArrayScreen
import com.github.gchudnov.swearwolf.util.Bytes.toHexStr
import com.github.gchudnov.swearwolf.util.{ Point, Size }
import com.github.gchudnov.swearwolf.woods.{ Resources, RichText }
import com.github.gchudnov.swearwolf.woods.text.RichTextSyntax.*
import zio.test.Assertion.{ equalTo, isLeft, isRight }
import zio.test.*

object RichTextSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("RichText")(
      // test("empty") {
      //   val input = ""

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = ""

      //   assert(actual)(equalTo(expected))
      // },
      // test("empty tag") {
      //   val input = "<bold></bold>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b316d1b5b32326d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("text without tags") {
      //   val input = "text"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "74657874"

      //   assert(actual)(equalTo(expected))
      // },
      // test("tag with text") {
      //   val input = "<i>text</i>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b336d746578741b5b32336d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("attribute with single quotes") {
      //   val input = "<color fg='#AABBCC'>text</color>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b33383b323b3137303b3138373b3230346d746578741b5b33396d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("attribute with double quotes") {
      //   val input = "<color fg=\"#AABBCC\" >text</color>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b33383b323b3137303b3138373b3230346d746578741b5b33396d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("multiple attributes") {
      //   val input = "<color fg='#AABBCC'  bg=\"DDEEFF\">text</color>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b33383b323b3137303b3138373b3230346d1b5b34383b323b3232313b3233383b3235356d746578741b5b34396d1b5b33396d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("nested tags") {
      //   val input = "<i><b>text</b></i>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b336d1b5b316d746578741b5b32326d1b5b32336d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("nested tags with text in between") {
      //   val input = "<i>A<b>text</b>B</i>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b336d411b5b316d746578741b5b32326d421b5b32336d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("nested tags with text and tags in between") {
      //   val input = "<i>A<b>text</b>B<u>C</u></i>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b336d411b5b316d746578741b5b32326d421b5b346d431b5b32346d1b5b32336d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("nested tags with text with spaces") {
      //   val input = "<i>A B<b>text</b>C </i>"

      //   val actual   = toHexStr(RichText.make(input).toTry.get.bytes.toSeq)
      //   val expected = "1b5b336d4120421b5b316d746578741b5b32326d43201b5b32336d"

      //   assert(actual)(equalTo(expected))
      // },
      // test("invalid document") {
      //   val input = "<i>no closing tag"

      //   val actual = RichText.make(input)

      //   assert(actual)(isLeft)
      // },
      // test("rich-text can be rendered") {
      //   val screen = ArrayScreen(Size(48, 32))
      //   val rich   = RichText.make("<b>BOLD</b><color fg='#AA0000' bg='#00FF00'>NOR</color>MAL<i>italic</i><k>BLINK</k>").toTry.get

      //   val actual = screen.put(Point(0, 0), rich)

      //   val actualData   = screen.toString
      //   val expectedData = Resources.string("text/text-rich.txt").toTry.get

      //   assert(actual)(isRight) &&
      //   assert(actualData)(equalTo(expectedData))
      // }
    )
