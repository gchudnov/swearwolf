package com.github.gchudnov.swearwolf.rich.internal.parser

import com.github.gchudnov.swearwolf.rich.internal.parser.Parser

import zio.test.Assertion.{ equalTo, isLeft }
import zio.test.*

object ParserSpec extends DefaultRunnableSpec:
  override def spec: ZSpec[Environment, Failure] =
    suite("ParserSpec")(
      test("input is empty") {
        val input    = ""
        val expected = Right(Seq.empty[Element])
        val actual   = Parser.parse(input)

        assert(actual)(equalTo(expected))
      }
    )

    // // Re, Ae, T1, T2, Be, T3, Bx, T4, Ax, Ce, T5, Cx, Rx
