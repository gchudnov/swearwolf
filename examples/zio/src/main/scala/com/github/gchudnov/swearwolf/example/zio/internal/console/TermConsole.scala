package com.github.gchudnov.swearwolf.example.zio.internal.console

import com.github.gchudnov.swearwolf.term.Term
import zio.Console
import zio.IO
import zio.ZTraceElement

import java.io.EOFException
import java.io.IOException
import java.io.PrintStream
import scala.Console as SConsole

final class TermConsole(term: Term) extends Console:

  override def print(line: => Any)(implicit trace: ZTraceElement): IO[IOException, Unit] =
    ???

  override def printError(line: => Any)(implicit trace: ZTraceElement): IO[IOException, Unit] =
    ???

  override def printLine(line: => Any)(implicit trace: ZTraceElement): IO[IOException, Unit] =
    ???

  override def printLineError(line: => Any)(implicit trace: ZTraceElement): IO[IOException, Unit] =
    ???

  override def readLine(implicit trace: ZTraceElement): IO[IOException, String] =
    ???

object TermConsole {}
