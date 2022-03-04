package com.github.gchudnov.swearwolf.example.zio.internal

import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.OutputStreamWriter
import java.io.OutputStream
import java.io.PrintWriter
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets.UTF_8

object Logger:
  def logLn(msg: String): Unit =
    val output = new FileOutputStream("/home/gchudnov/Projects/swearwolf/target/extra.txt", true)
    val pw     = new PrintWriter(new OutputStreamWriter(output, UTF_8))

    pw.println("EXTRA: " + msg)
    pw.flush()

    output.close()
