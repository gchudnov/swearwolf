package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.readers._

import scala.annotation.tailrec

/**
 * Reads bytes from buffer and returns (KeySeq, rest of the bytes that were not parsed)
 *
 * https://man7.org/linux/man-pages/man4/console_codes.4.html
 * https://en.wikipedia.org/wiki/ANSI_escape_code#CSI_sequences
 */
object Reader {

  private val readers = List[KeySeqReader](CharReader, CtrlReader, EscReader, MouseReader)

  def consume(bytes: Seq[Byte]): (Vector[KeySeq], Seq[Byte]) = {
    @tailrec
    def iterate(acc: Vector[KeySeq], xs: Seq[Byte]): (Vector[KeySeq], Seq[Byte]) =
      xs match {
        case ys if ys.nonEmpty =>
          val (k, rest) = anyRead(ys)
          k match {
            case UnknownKeySeq =>
              iterate(acc.appended(UnfamiliarKeySeq(xs)), Seq.empty[Byte])
            case PartialKeySeq =>
              (acc, ys)
            case keySeq =>
              iterate(acc.appended(keySeq), rest)
          }
        case _ =>
          (acc, Seq.empty[Byte])
      }

    iterate(Vector.empty[KeySeq], bytes)
  }

  private def anyRead(bytes: Seq[Byte]): (KeySeq, Seq[Byte]) = {
    val state: (KeySeq, Seq[Byte]) = (UnknownKeySeq, Seq.empty[Byte])
    readers.foldLeft(state) { (acc, reader) =>
      val res = reader.read(bytes)
      mergeReadResults(acc, res)
    }
  }

  private def isSeqFound(ks: KeySeq) =
    !(ks == UnknownKeySeq || ks == PartialKeySeq)

  private def mergeReadResults(lhs: (KeySeq, Seq[Byte]), rhs: (KeySeq, Seq[Byte])): (KeySeq, Seq[Byte]) =
    if (isSeqFound(lhs._1))
      lhs
    else if (isSeqFound(rhs._1))
      rhs
    else if (lhs._1 == PartialKeySeq)
      lhs
    else
      rhs
}
