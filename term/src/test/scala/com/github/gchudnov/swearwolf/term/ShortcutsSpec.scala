package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.keys.KeyCode
import zio.test.Assertion.*
import zio.test.*

object ShortcutsSpec extends ZIOSpecDefault:
  override def spec: Spec[TestEnvironment, Any] =
    suite("Shortcuts")(
      test("combination with unrecognized keys") {
        val keys = Seq(KeyCode.Space, KeyCode.Shift, KeyCode.Bell)

        val actual   = Shortcuts.keyCombination(keys)
        val expected = "SPACE + ⇧ + ?"

        assert(actual)(equalTo(expected))
      },
      test("combination") {
        val keys = Seq(KeyCode.Space, KeyCode.Shift, KeyCode.Up)

        val actual   = Shortcuts.keyCombination(keys)
        val expected = "SPACE + ⇧ + ↑"

        assert(actual)(equalTo(expected))
      },
    )
