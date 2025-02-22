package com.github.gchudnov.swearwolf.term.terms

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.MonadError

import java.io.{ BufferedOutputStream, InputStream, OutputStream }
import java.nio.file.Path
import java.time.format.DateTimeFormatter

/**
 * Any Terminal
 */
abstract class AnyTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(using ME: MonadError[F]) extends Term[F]:
  import MonadError.*

  private val bufOut = new BufferedOutputStream(out, AnyTerm.bufOutSize)

  override def read(): F[Option[Array[Byte]]] =
    for
      maybeFirstByte <- ME.blocking(nextChunk(1))
      allBytes <- maybeFirstByte match
                    case Some(first) =>
                      for
                        maybeLastBytes <- ME.blocking(nextAvailableChunk())
                        allBytes <- maybeLastBytes match
                                      case Some(last) =>
                                        ME.attempt(Some(first ++ last))
                                      case None =>
                                        ME.attempt(Some(first))
                      yield allBytes
                    case None =>
                      ME.attempt(None)
    yield allBytes

  override def write(bytes: Array[Byte]): F[Unit] =
    ME.attempt(out.write(bytes))

  override def flush(): F[Unit] =
    ME.attempt(out.flush())

  override def close(): F[Unit] =
    if isClose then
      val inF  = ME.attempt(in.close())
      val outF = ME.attempt(out.close())
      ME.ensure(inF, outF)
    else ME.succeed(())

  private def nextChunk(maxLen: Int): Option[Array[Byte]] =
    val data = new Array[Byte](maxLen)
    val n    = in.read(data, 0, data.length)
    n match
      case n if n > 0 =>
        Some(data.take(n))
      case -1 =>
        None // EOF
      case 0 =>
        Some(Array.empty[Byte])
      case x =>
        sys.error(s"Unexpected number of bytes were read: $x")

  private def nextAvailableChunk(): Option[Array[Byte]] =
    val nAvail = in.available()
    if nAvail > 0 then nextChunk(nAvail)
    else Some(Array.emptyByteArray)

object AnyTerm:
  private val bufOutSize: Int = 4096
