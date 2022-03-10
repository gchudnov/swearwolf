package com.github.gchudnov.swearwolf.util.func

import scala.collection.BuildFrom
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.util.control.Exception.*

given EitherMonad: MonadError[Either[Throwable, *]] with
  type R[+A] = Either[Throwable, A]

  override def succeed[A](a: A): R[A] =
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

  override def attempt[A](a: => A): Either[Throwable, A] = 
    allCatch.either(a)

  override def ensure[A](f: Either[Throwable, A], e: => Either[Throwable, Unit]): Either[Throwable, A] =
    def cleanup: Either[Throwable, Unit] =
      Try(e) match
        case Failure(t) => Left(t)
        case Success(x) => x

    f match
      case Left(t)  => cleanup.flatMap(_ => Left(t))
      case Right(a) => cleanup.map(_ => a)

  override def sequence[A, CC[+A] <: Iterable[A]](xs: CC[Either[Throwable, A]])(using bf: BuildFrom[CC[Either[Throwable, A]], A, CC[A]]): Either[Throwable, CC[A]] =
    val cbf = bf.toFactory(xs)
    xs.partitionMap(identity) match
      case (Nil, rights) => Right[Throwable, CC[A]](cbf.fromSpecific(rights))
      case (lefts, _)    => Left[Throwable, CC[A]](lefts.head)

  def traverse[A, CC[+A] <: Iterable[A], E, B](xs: CC[A])(f: A => Either[E, B])(using bf: BuildFrom[CC[A], B, CC[B]]): Either[E, CC[B]] =
    val builder = bf.newBuilder(xs)
    val i       = xs.iterator
    while i.hasNext do
      f(i.next) match
        case Right(b) => builder += b
        case Left(e)  => return Left(e)
    Right(builder.result)
