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

  def sequence[A, CC[A] <: Iterable[A], E](xs: CC[Either[E, A]])(implicit cbf: Factory[A, CC[A]]): Either[E, CC[A]] =
    xs.partitionMap(identity) match
      case (Nil, rights) => Right[E, CC[A]](cbf.fromSpecific(rights))
      case (lefts, _)    => Left[E, CC[A]](lefts.head)

  def traverse[A, CC[A] <: Iterable[A], E, B](xs: CC[A])(f: A => Either[E, B])(implicit cbf: Factory[B, CC[B]]): Either[E, CC[B]] =
    val builder = cbf.newBuilder
    val i       = xs.iterator
    while i.hasNext do
      f(i.next) match
        case Right(b) => builder += b
        case Left(e)  => return Left(e)
    Right(builder.result)
