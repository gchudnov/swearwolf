package com.github.gchudnov.swearwolf.util.func

import com.github.gchudnov.swearwolf.util.func.FunctionK

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

  def sequence[A, B](es: Seq[Either[A, B]]): Either[A, Seq[B]] =
    es.partitionMap(identity) match
      case (Nil, rights) => Right[A, Seq[B]](rights)
      case (lefts, _)    => Left[A, Seq[B]](lefts.head)
