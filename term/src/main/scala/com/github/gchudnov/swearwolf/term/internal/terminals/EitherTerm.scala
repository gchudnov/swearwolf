package com.github.gchudnov.swearwolf.term.internal.terminals

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.Identity
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import scala.util.control.Exception.allCatch

/**
 * Synchronous Term that wraps exceptions in Either[Throwable, *]
 */
final class EitherTerm(delegate: Term[Identity]) extends Term[Either[Throwable, *]]:

  override def read(): Either[Throwable, Option[Array[Byte]]] =
    allCatch.either(delegate.read())

  override def write(bytes: Array[Byte]): Either[Throwable, Unit] =
    allCatch.either(delegate.write(bytes))

  override def flush(): Either[Throwable, Unit] = 
    allCatch.either(delegate.flush())

  override def close(): Either[Throwable, Unit] =
    allCatch.either(delegate.close())
