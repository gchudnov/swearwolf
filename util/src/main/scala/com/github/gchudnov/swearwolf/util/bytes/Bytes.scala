package com.github.gchudnov.swearwolf.util.bytes

import java.nio.charset.StandardCharsets
import com.github.gchudnov.swearwolf.util.show.Show

final case class Bytes(value: Array[Byte]) extends AnyVal:

  def head: Byte =
    value.head

  def tail: Bytes =
    Bytes(value.tail)

  def isEmpty: Boolean =
    value.isEmpty

  def nonEmpty: Boolean =
    value.nonEmpty

  def size: Int =
    value.size

  def asString: String =
    new String(value, StandardCharsets.UTF_8)

  def asHex: String =
    value.map(b => f"$b%02x").mkString("")

object Bytes:

  val empty: Bytes =
    Bytes(Array.empty[Byte])

  def apply(byte: Byte): Bytes =
    Bytes(Array(byte))

  object +: { 
    def unapply(bytes: Bytes): Option[(Byte, Bytes)] = 
      if(bytes.nonEmpty) then
        Some(bytes.head, bytes.tail) 
      else
        None
  }

  extension (bytes: Bytes)
    def +(other: Bytes): Bytes =
      Bytes(bytes.value ++ other.value)

    def +:(b: Byte): Bytes =
      Bytes(b +: bytes.value)

  extension (str: String)
    def asBytes: Bytes =
      Bytes(str.grouped(2).map(Integer.parseInt(_, 16).toByte).toArray)

  given showBytes: Show[Bytes] with
    extension (a: Bytes)
      def show: String =
        a.value.map(b => f"\x$b%02x").mkString("")
