package com.github.gchudnov.swearwolf.util.func

import com.github.gchudnov.swearwolf.util.func.FunctionK
import scala.collection.Factory
import scala.collection.Iterable

object EitherOps:

  val eitherToId: FunctionK[Either[Throwable, *], Identity] =
    new FunctionK[Either[Throwable, *], Identity]:
      override def apply[A](fa: Either[Throwable, A]): Identity[A] =
        fa match
          case Left(e)  => throw e
          case Right(v) => v

  val idToEither: FunctionK[Identity, Either[Throwable, *]] =
    new FunctionK[Identity, Either[Throwable, *]]:
      override def apply[A](fa: Identity[A]): Either[Throwable, A] = Right(fa)
