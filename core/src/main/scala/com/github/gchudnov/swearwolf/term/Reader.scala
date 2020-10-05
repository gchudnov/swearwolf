package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.{ KeySeq, UnfamiliarKeySeq }
import com.github.gchudnov.swearwolf.term.readers._

import scala.annotation.tailrec

/**
 * Reads bytes from buffer and returns (KeySeq, rest of the bytes that were not parsed)
 *
 * https://man7.org/linux/man-pages/man4/console_codes.4.html
 * https://en.wikipedia.org/wiki/ANSI_escape_code#CSI_sequences
 */
private[term] object Reader {

  private val readers = List[KeySeqReader](CharReader, CtrlReader, EscReader, MouseReader)

  def consume(bytes: Seq[Byte]): (Vector[KeySeq], Seq[Byte]) = {
    @tailrec
    def iterate(acc: Vector[KeySeq], xs: Seq[Byte]): (Vector[KeySeq], Seq[Byte]) =
      xs match {
        case ys if ys.nonEmpty =>
          val rs = anyRead(ys)
          rs match {
            case UnknownReadState(_) =>
              iterate(acc.appended(UnfamiliarKeySeq(xs)), Seq.empty[Byte])
            case PartialReadState(_) =>
              (acc, ys)
            case ParsedReadState(keqSeq, rest) =>
              iterate(acc.appended(keqSeq), rest)
          }
        case _ =>
          (acc, Seq.empty[Byte])
      }

    iterate(Vector.empty[KeySeq], bytes)
  }

  private def anyRead(bytes: Seq[Byte]): ReadState =
    readers.foldLeft(ReadState.empty) { (acc, reader) =>
      val res = reader.read(bytes)
      mergeReadResults(acc, res)
    }

  private def mergeReadResults(lhs: ReadState, rhs: ReadState): ReadState = (lhs, rhs) match {
    case (ParsedReadState(_, _), _) =>
      lhs
    case (_, ParsedReadState(_, _)) =>
      rhs
    case (PartialReadState(_), _) =>
      lhs
    case _ =>
      rhs
  }
}
