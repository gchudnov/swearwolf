package com.github.gchudnov.swearwolf.util

import java.nio.charset.StandardCharsets

object Bytes:

  def toString(bytes: Seq[Byte]): String =
    new String(bytes.toArray, StandardCharsets.UTF_8)

  /**
   * Converts an array of bytes to a string
   * @param bytes
   *   an array of bytes
   * @return
   *   String
   */
  def toHexStr(bytes: Seq[Byte]): String =
    val sb = new StringBuilder
    for b <- bytes do sb.append(String.format("%02x", Byte.box(b)))
    sb.toString

  /**
   * Converts a hex-string to an array of bytes
   */
  def fromHexStr(value: String): Seq[Byte] =
    value.grouped(2).map(Integer.parseInt(_, 16).toByte).toSeq
