package com.github.gchudnov.swearwolf.util.bytes

import java.nio.charset.StandardCharsets
import com.github.gchudnov.swearwolf.util.show.Show

final case class Bytes(value: Seq[Byte]) extends AnyVal:
  def asString: String =
    new String(value.toArray, StandardCharsets.UTF_8)

  def asHex: String =
    value.map(b => f"$b%02x").mkString("")

object Bytes:

  val empty: Bytes =
    Bytes(Seq.empty[Byte])

  extension (str: String)
    def asBytes: Bytes =
      Bytes(str.grouped(2).map(Integer.parseInt(_, 16).toByte).toSeq)

  given showBytes: Show[Bytes] with
    extension (a: Bytes)
      def show: String =
        a.value.map(b => f"\x$b%02x").mkString("")
