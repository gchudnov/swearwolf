package com.github.gchudnov.swearwolf.term

import com.github.gchudnov.swearwolf.util.func.MonadAsyncError
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel
import java.nio.channels.Channels
import java.io.BufferedOutputStream

/**
 * Base Asynchronous Terminal
 */
abstract class AsyncTerm[F[_]](in: InputStream, out: OutputStream, isClose: Boolean)(implicit val monad: MonadAsyncError[F]) extends Term[F] {

  private val bufOutSize: Int = 4096
  private val bufOut = new BufferedOutputStream(out, bufOutSize)

  override def read(): F[Option[Array[Byte]]] =
    ???

  override def write(bytes: Array[Byte]): F[Unit] =
    ???

  override def close(): F[Unit] =
    if (isClose) then 
      val inF = monad.eval(in.close())
      val outF = monad.eval(out.close())
      monad.ensure(inF, outF)
    else
      monad.unit(())

}
