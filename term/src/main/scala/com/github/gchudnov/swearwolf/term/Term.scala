package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.internal.IOTerm
import com.github.gchudnov.swearwolf.term.keys.KeySeq

import java.io.BufferedOutputStream

// TODO: is it worth to move poll to the Screen and instead operate with Bytes here?

trait Term[F[_]]:
  def write(bytes: Array[Byte]): F[Unit]

  def write(seq: EscSeq): F[Unit] =
    write(seq.bytes)

  def write(str: String): F[Unit] =
    write(str.getBytes())

  def flush(): F[Unit]

  def poll(): F[Option[List[KeySeq]]]

object Term:
  private val OutBufferSizeBytes = 4096

  def make(): Term =
    val is  = System.in
    val out = System.out
    val os  = new BufferedOutputStream(out, OutBufferSizeBytes)

    new IOTerm(is, os)

    // TODO: should we add read / readLn ??
