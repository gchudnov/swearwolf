package com.github.gchudnov.swearwolf.rich.internal.parser

import com.github.gchudnov.swearwolf.rich.internal.parser.Lexer

import zio.test.Assertion.{ equalTo, isLeft }
import zio.test.*

object LexerSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("Lexer")(
      test("input is empty") {
        val input    = ""
        val expected = List.empty[Token]
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is whitespace") {
        val input    = "  \n\t\r"
        val expected = List(Text("  "), NewLine, Text("\t"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a single word") {
        val input    = "word"
        val expected = List(Text("word"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a single word with whitespace") {
        val input    = "word  "
        val expected = List(Text("word  "))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a single word with whitespace and newline") {
        val input    = "word  \n"
        val expected = List(Text("word  "), NewLine)
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is an opening tag without attributes") {
        val input    = "<tag>"
        val expected = List(OpenTag("tag"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is an opening tag with attribute enclosed in double quotes") {
        val input    = "<tag=\"value1\">"
        val expected = List(OpenTag("tag", Some("value1")))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is an opening tag with attribute enclosed in single quotes") {
        val input    = "<tag='value1'>"
        val expected = List(OpenTag("tag", Some("value1")))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a tag where attribute is without quotes") {
        val input    = "<tag=value1>"
        val expected = List(OpenTag("tag", Some("value1")))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a closing tag") {
        val input    = "</tag>"
        val expected = List(CloseTag("tag"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is an opening and closing tag with text") {
        val input    = "<tag>text</tag>"
        val expected = List(OpenTag("tag"), Text("text"), CloseTag("tag"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is an opening and closing tag without text") {
        val input    = "<tag></tag>"
        val expected = List(OpenTag("tag"), CloseTag("tag"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a sequence of nested opening and closing tags") {
        val input    = "<tag1><tag2></tag2></tag1>"
        val expected = List(OpenTag("tag1"), OpenTag("tag2"), CloseTag("tag2"), CloseTag("tag1"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a sequence of nested opening and closing tags with text") {
        val input    = "<tag1><tag2>text</tag2></tag1>"
        val expected = List(OpenTag("tag1"), OpenTag("tag2"), Text("text"), CloseTag("tag2"), CloseTag("tag1"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a sequence of tags with text") {
        val input    = "<tag1>text1</tag1><tag2>text2</tag2>"
        val expected = List(OpenTag("tag1"), Text("text1"), CloseTag("tag1"), OpenTag("tag2"), Text("text2"), CloseTag("tag2"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a angle bracket") {
        val input    = "<"
        val expected = List(Text("<"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is an opening angle bracket with text") {
        val input    = "<text"
        val expected = List(Text("<"), Text("text"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a closing angle bracket with text") {
        val input    = ">text"
        val expected = List(Text(">text"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is a tag with angle brackets inside") {
        val input    = "<tag>text 1<2 </tag>"
        val expected = List(OpenTag("tag"), Text("text 1"), Text("<"), Text("2 "), CloseTag("tag"))
        val actual   = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      },
      test("input is several nested tags with attributes") {
        val input = "<tag1 = \"value1\">text1<tag2='value3'></tag2></tag1>"
        val expected = List(
          OpenTag("tag1", Some("value1")),
          Text("text1"),
          OpenTag("tag2", Some("value3")),
          CloseTag("tag2"),
          CloseTag("tag1")
        )

        val actual = Lexer.lex(input)

        assert(actual)(equalTo(expected))
      }
    )
