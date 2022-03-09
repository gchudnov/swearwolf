package com.github.gchudnov.swearwolf.util.func

import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.collection.Factory

object EitherMonad extends MonadError[Either[Throwable, *]]:
  type R[+A] = Either[Throwable, A]

  override def pure[A](a: A): R[A] =
    Right(a)

  override def map[A, B](ra: R[A])(f: A => B): R[B] =
    ra match
      case Right(a) => Right(f(a))
      case _        => ra.asInstanceOf[R[B]]

  override def flatMap[A, B](fa: R[A])(f: A => R[B]): R[B] =
    fa match
      case Right(a) => f(a)
      case _        => fa.asInstanceOf[R[B]]

  override def error[A](t: Throwable): R[A] =
    Left(t)

  override protected def handleErrorWith_[A](ra: R[A])(h: PartialFunction[Throwable, R[A]]): R[A] =
    ra match
      case Left(a) if h.isDefinedAt(a) => h(a)
      case _                           => ra

  override def ensure[A](f: Either[Throwable, A], e: => Either[Throwable, Unit]): Either[Throwable, A] =
    def cleanup: Either[Throwable, Unit] =
      Try(e) match
        case Failure(t) => Left(t)
        case Success(x) => x

    f match
      case Left(t)  => cleanup.flatMap(_ => Left(t))
      case Right(a) => cleanup.map(_ => a)

  override def sequence[A, CC[A] <: Iterable[A]](xs: CC[Either[Throwable, A]])(implicit cbf: Factory[A, CC[A]]): Either[Throwable, CC[A]] =
    xs.partitionMap(identity) match
      case (Nil, rights) => Right[Throwable, CC[A]](cbf.fromSpecific(rights))
      case (lefts, _)    => Left[Throwable, CC[A]](lefts.head)
