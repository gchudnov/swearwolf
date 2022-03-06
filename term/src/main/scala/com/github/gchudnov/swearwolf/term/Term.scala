package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.keys.KeySeq

import java.io.BufferedOutputStream

trait Term[F[_]]:
  def read(): F[Option[Array[Byte]]]
  def write(bytes: Array[Byte]): F[Unit]
  def flush(): F[Unit]
  def close(): F[Unit]

object Term:
  // private val OutBufferSizeBytes = 4096

  // def make(): Term =
  //   val is  = System.in
  //   val out = System.out
  //   val os  = new BufferedOutputStream(out, OutBufferSizeBytes)

  //   new IOTerm(is, os)

    // TODO: should we add read / readLn ??


//   def poll(): F[Option[List[KeySeq]]] -- move somewhere else