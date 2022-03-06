package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.internal.terminals.IOTerm
import com.github.gchudnov.swearwolf.util.func.MonadError
import java.io.InputStream
import java.io.OutputStream
import java.io.BufferedOutputStream

/**
 * Synchronous Terminal
 */
abstract class SyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(implicit val ME: MonadError[F]) extends Term[F]:
  import MonadError.*

  private val bufOutSize: Int = 4096
  private val bufOut          = new BufferedOutputStream(out, bufOutSize)

  override def read(): F[Option[Array[Byte]]] =
    for
      maybeFirstByte <- ME.blocking(IOTerm.nextChunk(in, 1))
      allBytes <- maybeFirstByte match
                    case Some(first) =>
                      for
                        maybeLastBytes <- ME.blocking(IOTerm.nextAvailableChunk(in))
                        allBytes <- maybeLastBytes match
                                      case Some(last) =>
                                        ME.eval(Some(first ++ last))
                                      case None =>
                                        ME.eval(Some(first))
                      yield allBytes
                    case None =>
                      ME.eval(None)
    yield allBytes

  override def write(bytes: Array[Byte]): F[Unit] =
    ME.eval(out.write(bytes))

  override def flush(): F[Unit] =
    ME.eval(out.flush())

  override def close(): F[Unit] =
    if (isClose) then
      val inF  = ME.eval(in.close())
      val outF = ME.eval(out.close())
      ME.ensure(inF, outF)
    else ME.unit(())
