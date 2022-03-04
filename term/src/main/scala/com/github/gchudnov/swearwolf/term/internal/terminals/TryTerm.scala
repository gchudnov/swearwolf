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

  override def write(bytes: Array[Byte]): Try[Unit] =
    Try(delegate.write(bytes))

  override def flush(): Try[Unit] =
    Try(delegate.flush())

  override def poll(): Try[Option[List[KeySeq]]] =
    Try(delegate.poll())

  private val tryToId: FunctionK[Try, Identity] =
    new FunctionK[Try, Identity]:
      override def apply[A](fa: Try[A]): Identity[A] = fa.get

  private val idToTry: FunctionK[Identity, Try] =
    new FunctionK[Identity, Try]:
      override def apply[A](fa: Identity[A]): Try[A] = Try(fa)
