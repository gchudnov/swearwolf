package com.github.gchudnov.swearwolf.term.internal.terminals

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.Identity
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import scala.util.Try
import com.github.gchudnov.swearwolf.util.func.FunctionK

/**
 * Synchronous terminal that wraps exceptions in Try.
 */
final class TryTerm(delegate: Term[Identity]) extends Term[Try]:

  override def read(): Try[Option[Array[Byte]]] =
    Try(delegate.read())

  override def write(bytes: Array[Byte]): Try[Unit] =
    Try(delegate.write(bytes))

  override def flush(): Try[Unit] =
    Try(delegate.flush())

  override def close(): Try[Unit] =
    Try(delegate.close())
