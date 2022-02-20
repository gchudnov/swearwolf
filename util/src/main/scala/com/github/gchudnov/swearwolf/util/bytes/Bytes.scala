package com.github.gchudnov.swearwolf.util.bytes

import java.nio.charset.StandardCharsets

final case class Bytes(value: Seq[Byte]) extends AnyVal

object Bytes:

  extension (bytes: Bytes)
    def asString: String =
      new String(bytes.value.toArray, StandardCharsets.UTF_8)

    def asHexString: String =
      bytes.value.map(b => f"$b%02x").mkString("")

  extension (str: String)
    def asBytes: Bytes =
      Bytes(str.grouped(2).map(Integer.parseInt(_, 16).toByte).toSeq)
