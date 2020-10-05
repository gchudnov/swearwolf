package com.github.gchudnov.woods.util

import com.github.gchudnov.swearwolf.KeyCode
import zio.test.Assertion._
import zio.test._

object SymbolsSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] =
    suite("Symbols")(
      test("combination with unrecognized keys") {
        val keys = Seq(KeyCode.Space, KeyCode.Shift, KeyCode.Bell)

        val actual   = Symbols.keyCombination(keys)
        val expected = "SPACE + ⇧ + ?"

        assert(actual)(equalTo(expected))
      },
      test("combination") {
        val keys = Seq(KeyCode.Space, KeyCode.Shift, KeyCode.Up)

        val actual   = Symbols.keyCombination(keys)
        val expected = "SPACE + ⇧ + ↑"

        assert(actual)(equalTo(expected))
      }
    )

}
