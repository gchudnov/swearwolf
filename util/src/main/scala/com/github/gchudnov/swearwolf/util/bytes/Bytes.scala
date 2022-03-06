package com.github.gchudnov.swearwolf.util.bytes

import com.github.gchudnov.swearwolf.util.show.Show

import java.nio.charset.StandardCharsets
import scala.language.strictEquality

final class Bytes(value: Array[Byte]) extends AnyVal derives CanEqual:

  override def equals(other: Any): Boolean = other match
    case that: Bytes => value.sameElements(that.asArray)
    case _           => false

  override def hashCode(): Int = value.hashCode()

  override def toString: String =
    val bs = value.map(b => f"$b%02x").mkString("[", ", ", "]")
    s"Bytes($bs)"

  def head: Byte =
    value.head

  def tail: Bytes =
    new Bytes(value.tail)

  def isEmpty: Boolean =
    value.isEmpty

  def nonEmpty: Boolean =
    value.nonEmpty

  def size: Int =
    value.size

  def asArray: Array[Byte] =
    value.clone()

  def asString: String =
    new String(value, StandardCharsets.UTF_8)

  def asHex: String =
    value.map(b => f"$b%02x").mkString("")

object Bytes:

  val empty: Bytes =
    new Bytes(Array.empty[Byte])

  def apply(bytes: Array[Byte]): Bytes =
    new Bytes(bytes)

  def apply(bytes: Seq[Byte]): Bytes =
    new Bytes(bytes.toArray)    

  def apply(byte: Byte): Bytes =
    new Bytes(Array(byte))

  object +: :
    def unapply(bytes: Bytes): Option[(Byte, Bytes)] =
      if (bytes.nonEmpty) then Some(bytes.head, bytes.tail)
      else None

  extension (bytes: Bytes)
    def +(other: Bytes): Bytes =
      new Bytes(bytes.asArray ++ other.asArray)

    def +:(b: Byte): Bytes =
      new Bytes(b +: bytes.asArray)

  extension (str: String)
    def asBytes: Bytes =
      Bytes(str.grouped(2).map(Integer.parseInt(_, 16).toByte).toArray)

  given showBytes: Show[Bytes] with
    extension (a: Bytes)
      def show: String =
        val text = a.asString.replaceAll("\\p{C}", ".");
        val hex  = a.asArray.map(b => f"$b%02x").mkString(" ")
        s"|${hex}|${text}|"
