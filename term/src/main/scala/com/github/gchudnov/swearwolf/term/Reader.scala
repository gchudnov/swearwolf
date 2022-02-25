package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.{ KeySeq, UnknownKeySeq }
import com.github.gchudnov.swearwolf.term.internal.readers.*

import scala.annotation.tailrec
import com.github.gchudnov.swearwolf.util.bytes.Bytes

/**
 * Reads bytes from buffer and returns (KeySeq, rest of the bytes that were not parsed)
 *
 * https://man7.org/linux/man-pages/man4/console_codes.4.html
 *
 * https://en.wikipedia.org/wiki/ANSI_escape_code#CSI_sequences
 */
private[term] object Reader:

  private val readers = List[KeySeqReader](CharReader, CtrlReader, EscReader, MouseReader)

  def consume(bytes: Bytes): (Vector[KeySeq], Bytes) =
    @tailrec
    def iterate(acc: Vector[KeySeq], xs: Bytes): (Vector[KeySeq], Bytes) =
      xs match
        case ys if ys.nonEmpty =>
          val rs = anyRead(ys)
          rs match
            case UnknownReadState(_) =>
              iterate(acc.appended(UnknownKeySeq(xs)), Bytes.empty)
            case PartialReadState(_) =>
              (acc, ys)
            case ParsedReadState(keqSeq, rest) =>
              iterate(acc.appended(keqSeq), rest)
        case _ =>
          (acc, Bytes.empty)

    iterate(Vector.empty[KeySeq], bytes)

  private def anyRead(bytes: Bytes): ReadState =
    readers.foldLeft(ReadState.empty) { (acc, reader) =>
      val res = reader.read(bytes)
      mergeReadResults(acc, res)
    }

  private def mergeReadResults(lhs: ReadState, rhs: ReadState): ReadState = (lhs, rhs) match
    case (ParsedReadState(_, _), _) =>
      lhs
    case (_, ParsedReadState(_, _)) =>
      rhs
    case (PartialReadState(_), _) =>
      lhs
    case _ =>
      rhs
