package com.github.gchudnov.swearwolf.example.zio.internal.console

import com.github.gchudnov.swearwolf.term.Term
import zio.Console
import zio.IO
import zio.ZIO
import zio.ZTraceElement

import java.io.EOFException
import java.io.IOException
import java.io.PrintStream
import scala.Console as SConsole
import zio.ZLayer

final class TermConsole()

// final class TermConsole(term: Term) extends Console:
//   import TermConsole.*

//   override def print(line: => Any)(implicit trace: ZTraceElement): IO[IOException, Unit] =
//     printToStdOut(line)

//   override def printError(line: => Any)(implicit trace: ZTraceElement): IO[IOException, Unit] =
//     printToStdErr(line)

//   override def printLine(line: => Any)(implicit trace: ZTraceElement): IO[IOException, Unit] =
//     printToStdOut(line.toString + lineSeparator)

//   override def printLineError(line: => Any)(implicit trace: ZTraceElement): IO[IOException, Unit] =
//     printToStdErr(line.toString + lineSeparator)

//   override def readLine(implicit trace: ZTraceElement): IO[IOException, String] =
//     ??? // NOT implemented

//   private def printToStdOut(line: => Any): IO[IOException, Unit] =
//     ZIO.fromEither(term.write(line.toString)).mapError(t => toIOException(t))

//   private def printToStdErr(line: => Any): IO[IOException, Unit] =
//     printToStdOut(line)

// object TermConsole:
//   private val lineSeparator = sys.props("line.separator")

//   val layer: ZLayer[Term, Throwable, Console] =
//     (for
//       term   <- ZIO.service[Term]
//       console = new TermConsole(term)
//     yield console).toLayer

//   private def toIOException(t: Throwable): IOException =
//     t match
//       case e: IOException => e
//       case _              => new IOException(t)
