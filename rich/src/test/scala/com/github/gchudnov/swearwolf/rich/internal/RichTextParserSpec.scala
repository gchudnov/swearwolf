package com.github.gchudnov.swearwolf.rich.internal

import RichTextParser.*
import zio.test.Assertion.{ equalTo, isLeft }
import zio.test.*

object RichTextParserSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("RichTextParser")(
      // test("empty") {
      //   val input = ""

      //   val actual   = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq(name = "root")

      //   assert(actual)(equalTo(expected))
      // },
      // test("empty tag") {
      //   val input = "<bold></bold>"

      //   val actual   = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq(name = "root", inner = List[Block](NamedBlockSeq(name = "bold", inner = List.empty[Block])))

      //   assert(actual)(equalTo(expected))
      // },
      // test("text without tags") {
      //   val input = "text"

      //   val actual   = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq(name = "root", inner = List[Block](TextBlock("text")))

      //   assert(actual)(equalTo(expected))
      // },
      // test("tag with text") {
      //   val input = "<i>text</i>"

      //   val actual   = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq(name = "root", inner = List[Block](NamedBlockSeq(name = "i", inner = List[Block](TextBlock("text")))))

      //   assert(actual)(equalTo(expected))
      // },
      // test("attribute with single quotes") {
      //   val input = "<color fg='#AABBCC'>text</color>"

      //   val actual   = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq("root", Seq.empty[Attr], List[Block](NamedBlockSeq("color", Seq(NamedAttr("fg", "#AABBCC")), List[Block](TextBlock("text")))))

      //   assert(actual)(equalTo(expected))
      // },
      // test("attribute with double quotes") {
      //   val input = "<color fg=\"#AABBCC\" >text</color>"

      //   val actual   = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq("root", Seq.empty[Attr], List[Block](NamedBlockSeq("color", Seq(NamedAttr("fg", "#AABBCC")), List[Block](TextBlock("text")))))

      //   assert(actual)(equalTo(expected))
      // },
      // test("attribute with named color") {
      //   val input = "<color fg='blue'>text</color>"

      //   val actual   = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq("root", Seq.empty[Attr], List[Block](NamedBlockSeq("color", Seq(NamedAttr("fg", "blue")), List[Block](TextBlock("text")))))

      //   assert(actual)(equalTo(expected))
      // },
      // test("multiple attributes") {
      //   val input = "<color fg='#AABBCC'  bg=\"DDEEFF\">text</color>"

      //   val actual = RichTextParser.read(input).toTry.get
      //   val expected =
      //     NamedBlockSeq("root", Seq.empty[Attr], List[Block](NamedBlockSeq("color", Seq(NamedAttr("fg", "#AABBCC"), NamedAttr("bg", "DDEEFF")), List[Block](TextBlock("text")))))

      //   assert(actual)(equalTo(expected))
      // },
      // test("nested tags") {
      //   val input = "<i><b>text</b></i>"

      //   val actual = RichTextParser.read(input).toTry.get
      //   val expected =
      //     NamedBlockSeq(name = "root", inner = List[Block](NamedBlockSeq(name = "i", inner = List[Block](NamedBlockSeq(name = "b", inner = List[Block](TextBlock("text")))))))

      //   assert(actual)(equalTo(expected))
      // },
      // test("nested tags with text in between") {
      //   val input = "<i>A<b>text</b>B</i>"

      //   val actual = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq(
      //     name = "root",
      //     inner = List[Block](NamedBlockSeq(name = "i", inner = List[Block](TextBlock("A"), NamedBlockSeq(name = "b", inner = List[Block](TextBlock("text"))), TextBlock("B"))))
      //   )

      //   assert(actual)(equalTo(expected))
      // },
      // test("nested tags with text and tags in between") {
      //   val input = "<i>A<b>text</b>B<u>C</u></i>"

      //   val actual = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq(
      //     name = "root",
      //     inner = List[Block](
      //       NamedBlockSeq(
      //         name = "i",
      //         inner = List[Block](
      //           TextBlock("A"),
      //           NamedBlockSeq(name = "b", inner = List[Block](TextBlock("text"))),
      //           TextBlock("B"),
      //           NamedBlockSeq(name = "u", inner = List[Block](TextBlock("C")))
      //         )
      //       )
      //     )
      //   )

      //   assert(actual)(equalTo(expected))
      // },
      // test("nested tags with text with spaces") {
      //   val input = "<i>A B<b>text</b>C </i>"

      //   val actual = RichTextParser.read(input).toTry.get
      //   val expected = NamedBlockSeq(
      //     name = "root",
      //     inner = List[Block](NamedBlockSeq(name = "i", inner = List[Block](TextBlock("A B"), NamedBlockSeq(name = "b", inner = List[Block](TextBlock("text"))), TextBlock("C "))))
      //   )

      //   assert(actual)(equalTo(expected))
      // },
      // test("invalid document") {
      //   val input = "<i>A B<b>"

      //   val actual = RichTextParser.read(input)

      //   assert(actual)(isLeft)
      // }
    )
