package com.github.gchudnov.swearwolf.term.internal.screens

import java.io.{ OutputStream, OutputStreamWriter, PrintWriter }
import java.nio.charset.StandardCharsets.UTF_8
import com.github.gchudnov.swearwolf.term.Screen
import com.github.gchudnov.swearwolf.util.geometry.Size
import com.github.gchudnov.swearwolf.util.geometry.Point
import com.github.gchudnov.swearwolf.util.spans.Span
import com.github.gchudnov.swearwolf.util.styles.TextStyle
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.term.EventLoop
import com.github.gchudnov.swearwolf.term.EventLoop.KeySeqHandler

/**
 * Log operations to an output stream.
 * 
 * val outStream = new FileOutputStream("/some/path/log.txt", true)
 */
private[screens] final class LogScreen(output: OutputStream) extends Screen:
  private val pw = new PrintWriter(new OutputStreamWriter(output, UTF_8))

  override def size: Size = ???

  override def put(pt: Point, value: String): Either[Throwable, Unit]                   = ???
  override def put(pt: Point, value: String, style: TextStyle): Either[Throwable, Unit] = ???
  override def put(pt: Point, value: Span): Either[Throwable, Unit]                     = ???
  override def put(pt: Point, value: Array[Byte]): Either[Throwable, Unit]              = ???

  override def cursorHide(): Either[Throwable, Unit] = ???
  override def cursorShow(): Either[Throwable, Unit] = ???

  override def mouseTrack(): Either[Throwable, Unit]   = ???
  override def mouseUntrack(): Either[Throwable, Unit] = ???

  override def bufferNormal(): Either[Throwable, Unit] = ???
  override def bufferAlt(): Either[Throwable, Unit]    = ???

  override def clear(): Either[Throwable, Unit] = ???

  override def flush(): Either[Throwable, Unit] = ???

  override def close(): Unit = ???

object LogScreen:
  def apply(output: OutputStream): LogScreen =
    new LogScreen(output)
