package com.github.gchudnov.swearwolf.woods.text.impl

import com.github.gchudnov.swearwolf.util.Color
import RichTextParser._
import RichTextStyler._
import zio.test.Assertion.{ equalTo, isLeft }
import zio.test._

object RichTestStylerSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("RichTestStyler")(
      test("empty") {
        val input = NamedBlockSeq(name = "root")

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq.empty[RichStyle])

        assert(actual)(equalTo(expected))
      },
      test("empty tag") {
        val input = NamedBlockSeq(name = "root", inner = List[Block](NamedBlockSeq(name = "bold", inner = List.empty[Block])))

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichBoldStyle(Seq.empty[RichStyle])))

        assert(actual)(equalTo(expected))
      },
      test("text without tags") {
        val input = NamedBlockSeq(name = "root", inner = List[Block](TextBlock("text")))

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichTextStyle("text")))

        assert(actual)(equalTo(expected))
      },
      test("tag with text") {
        val input = NamedBlockSeq(name = "root", inner = List[Block](NamedBlockSeq(name = "i", inner = List[Block](TextBlock("text")))))

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichItalicStyle(Seq(RichTextStyle("text")))))

        assert(actual)(equalTo(expected))
      },
      test("attribute with single quotes") {
        val input = NamedBlockSeq("root", Seq.empty[Attr], List[Block](NamedBlockSeq("color", Seq(NamedAttr("fg", "#AABBCC")), List[Block](TextBlock("text")))))

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichBoxStyle(Seq(RichForegroundStyle(Color(170, 187, 204), Seq(RichTextStyle("text")))))))

        assert(actual)(equalTo(expected))
      },
      test("attribute with double quotes") {
        val input = NamedBlockSeq("root", Seq.empty[Attr], List[Block](NamedBlockSeq("color", Seq(NamedAttr("bg", "#AABBCC")), List[Block](TextBlock("text")))))

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichBoxStyle(Seq(RichBackgroundStyle(Color(170, 187, 204), Seq(RichTextStyle("text")))))))

        assert(actual)(equalTo(expected))
      },
      test("attribute with named color") {
        val input = NamedBlockSeq("root", Seq.empty[Attr], List[Block](NamedBlockSeq("color", Seq(NamedAttr("bg", "blue")), List[Block](TextBlock("text")))))

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichBoxStyle(Seq(RichBackgroundStyle(Color(0, 0, 255), Seq(RichTextStyle("text")))))))

        assert(actual)(equalTo(expected))
      },
      test("multiple attributes") {
        val input =
          NamedBlockSeq("root", Seq.empty[Attr], List[Block](NamedBlockSeq("color", Seq(NamedAttr("fg", "#AABBCC"), NamedAttr("bg", "DDEEFF")), List[Block](TextBlock("text")))))

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichBoxStyle(Seq(RichForegroundStyle(Color(170, 187, 204), Seq(RichBackgroundStyle(Color(221, 238, 255), Seq(RichTextStyle("text")))))))))

        assert(actual)(equalTo(expected))
      },
      test("nested tags") {
        val input =
          NamedBlockSeq(name = "root", inner = List[Block](NamedBlockSeq(name = "i", inner = List[Block](NamedBlockSeq(name = "b", inner = List[Block](TextBlock("text")))))))

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichItalicStyle(Seq(RichBoldStyle(Seq(RichTextStyle("text")))))))

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text in between") {
        val input = NamedBlockSeq(
          name = "root",
          inner = List[Block](NamedBlockSeq(name = "i", inner = List[Block](TextBlock("A"), NamedBlockSeq(name = "b", inner = List[Block](TextBlock("text"))), TextBlock("B"))))
        )

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichItalicStyle(Seq(RichTextStyle("A"), RichBoldStyle(Seq(RichTextStyle("text"))), RichTextStyle("B")))))

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text and tags in between") {
        val input = NamedBlockSeq(
          name = "root",
          inner = List[Block](
            NamedBlockSeq(
              name = "i",
              inner = List[Block](
                TextBlock("A"),
                NamedBlockSeq(name = "b", inner = List[Block](TextBlock("text"))),
                TextBlock("B"),
                NamedBlockSeq(name = "u", inner = List[Block](TextBlock("C")))
              )
            )
          )
        )

        val actual = RichTextStyler.style(input).toTry.get
        val expected =
          RichBoxStyle(Seq(RichItalicStyle(Seq(RichTextStyle("A"), RichBoldStyle(Seq(RichTextStyle("text"))), RichTextStyle("B"), RichUnderlineStyle(Seq(RichTextStyle("C")))))))

        assert(actual)(equalTo(expected))
      },
      test("nested tags with text with spaces") {
        val input = NamedBlockSeq(
          name = "root",
          inner = List[Block](NamedBlockSeq(name = "i", inner = List[Block](TextBlock("A B"), NamedBlockSeq(name = "b", inner = List[Block](TextBlock("text"))), TextBlock("C "))))
        )

        val actual   = RichTextStyler.style(input).toTry.get
        val expected = RichBoxStyle(Seq(RichItalicStyle(Seq(RichTextStyle("A B"), RichBoldStyle(Seq(RichTextStyle("text"))), RichTextStyle("C ")))))

        assert(actual)(equalTo(expected))
      },
      test("unrecognized tag") {
        val input = NamedBlockSeq("invalid-name")

        val actual = RichTextStyler.style(input)

        assert(actual)(isLeft)
      }
    )

}
