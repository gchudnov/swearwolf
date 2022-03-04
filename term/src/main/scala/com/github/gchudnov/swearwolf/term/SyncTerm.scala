package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadError
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel
import java.nio.channels.Channels
import java.nio.ByteBuffer
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
      maybeFirstByte <- nextChunk(1)
      allBytes <- maybeFirstByte match
                    case Some(first) =>
                      for
                        maybeLastBytes <- nextAvailableChunk()
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

  // TODO: extract to utils
  private def nextChunk(maxLen: Int): F[Option[Array[Byte]]] =
    monad.blocking {
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
    }

  private def nextAvailableChunk(): F[Option[Array[Byte]]] =
    for
      nAvail <- monad.eval(in.available())
      maybeBytes <- if nAvail > 0 then nextChunk(nAvail)
                    else monad.eval(Some(Array.emptyByteArray))
    yield maybeBytes
