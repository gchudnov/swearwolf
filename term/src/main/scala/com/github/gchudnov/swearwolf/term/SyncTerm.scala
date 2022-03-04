package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.term.internal.terminals.IOTerm
import com.github.gchudnov.swearwolf.util.func.MonadError
import java.io.InputStream
import java.io.OutputStream
import java.io.BufferedOutputStream

/**
 * Synchronous Terminal
 */
abstract class SyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(implicit val monad: MonadError[F]) extends Term[F]:
  import MonadError.*

  private val bufOutSize: Int = 4096
  private val bufOut          = new BufferedOutputStream(out, bufOutSize)

  override def read(): F[Option[Array[Byte]]] =
    for
      maybeFirstByte <- monad.blocking(IOTerm.nextChunk(in, 1))
      allBytes <- maybeFirstByte match
                    case Some(first) =>
                      for
                        maybeLastBytes <- monad.blocking(IOTerm.nextAvailableChunk(in))
                        allBytes <- maybeLastBytes match
                                      case Some(last) =>
                                        monad.eval(Some(first ++ last))
                                      case None =>
                                        monad.eval(Some(first))
                      yield allBytes
                    case None =>
                      monad.eval(None)
    yield allBytes

  override def write(bytes: Array[Byte]): F[Unit] =
    monad.eval(out.write(bytes))

  override def flush(): F[Unit] =
    monad.eval(out.flush())

  override def close(): F[Unit] =
    if (isClose) then
      val inF  = monad.eval(in.close())
      val outF = monad.eval(out.close())
      monad.ensure(inF, outF)
    else monad.unit(())
