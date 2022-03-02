package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.internal.IOTerm
import com.github.gchudnov.swearwolf.term.keys.KeySeq

import java.io.BufferedOutputStream

trait Term:
  def write(bytes: Array[Byte]): Either[Throwable, Unit]
  def write(seq: EscSeq): Either[Throwable, Unit]
  def write(str: String): Either[Throwable, Unit]

  def flush(): Either[Throwable, Unit]

  def blockingPoll(): Either[Throwable, Option[List[KeySeq]]]
  def poll(): Either[Throwable, Option[List[KeySeq]]]

object Term:
  private val OutBufferSizeBytes = 131072

  def make(): Term =
    val is  = System.in
    val out = System.out
    val os  = new BufferedOutputStream(out, OutBufferSizeBytes)

    new IOTerm(is, os)

    // TODO: should we add read / readLn ??
