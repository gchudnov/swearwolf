package com.github.gchudnov.swearwolf.term

import java.io.BufferedOutputStream

import com.github.gchudnov.swearwolf.term.KeySeq

private[term] trait Term:
  def write(bytes: Array[Byte]): Either[Throwable, Unit]
  def write(seq: EscSeq): Either[Throwable, Unit]
  def write(str: String): Either[Throwable, Unit]

  def flush(): Either[Throwable, Unit]

  def blockingPoll(): Either[Throwable, List[KeySeq]]
  def poll(): Either[Throwable, List[KeySeq]]

private[term] object Term:
  private val OutBufferSizeBytes = 131072

  def make(): Term =
    val is = System.in
    val os = new BufferedOutputStream(System.out, OutBufferSizeBytes)

    new IOTerm(is, os)
