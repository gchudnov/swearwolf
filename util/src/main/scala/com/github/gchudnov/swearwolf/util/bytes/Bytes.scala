package com.github.gchudnov.swearwolf.util.bytes

import java.nio.charset.StandardCharsets
import com.github.gchudnov.swearwolf.util.show.Show
import scala.language.strictEquality

final case class Bytes(value: Array[Byte]) extends AnyVal derives CanEqual:

  override def equals(other: Any): Boolean = other match
    case that: Bytes => value.sameElements(that.value)
    case _           => false

  override def hashCode(): Int = value.hashCode()

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

  object +: :
    def unapply(bytes: Bytes): Option[(Byte, Bytes)] =
      if (bytes.nonEmpty) then Some(bytes.head, bytes.tail)
      else None

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
        val text = a.asString.replaceAll("\\p{C}", ".");
        val hex  = a.value.map(b => f"$b%02x").mkString(" ")
        s"|${hex}|${text}|"
