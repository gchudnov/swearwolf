package com.github.gchudnov.swearwolf.term.internal.terminals

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.Identity
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.func.FunctionK
import scala.util.control.Exception.allCatch

/**
 * Synchronous Term that wraps exceptions in Either[Throwable, *]
 */
final class EitherTerm(delegate: Term[Identity]) extends Term[Either[Throwable, *]]:

  override def write(bytes: Array[Byte]): Either[Throwable, Unit] =
    allCatch.either(delegate.write(bytes))

  override def flush(): Either[Throwable, Unit] =
    allCatch.either(delegate.flush())

  override def poll(): Either[Throwable, Option[List[KeySeq]]] =
    allCatch.either(delegate.poll())

  private val eitherToId: FunctionK[Either[Throwable, *], Identity] =
    new FunctionK[Either[Throwable, *], Identity]:
      override def apply[A](fa: Either[Throwable, A]): Identity[A] =
        fa match
          case Left(e)  => throw e
          case Right(v) => v

  private val idToEither: FunctionK[Identity, Either[Throwable, *]] =
    new FunctionK[Identity, Either[Throwable, *]]:
      override def apply[A](fa: Identity[A]): Either[Throwable, A] = Right(fa)
