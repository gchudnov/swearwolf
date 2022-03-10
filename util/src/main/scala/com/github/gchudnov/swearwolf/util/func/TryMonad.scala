package com.github.gchudnov.swearwolf.util.func

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.collection.BuildFrom

given TryMonad: MonadError[Try] with

  override def succeed[A](a: A): Try[A] =
    Success(a)

  override def map[A, B](ta: Try[A])(f: (A) => B): Try[B] =
    ta.map(f)

  override def flatMap[A, B](ta: Try[A])(f: (A) => Try[B]): Try[B] =
    ta.flatMap(f)

  override def error[A](t: Throwable): Try[A] =
    Failure(t)

  override protected def handleErrorWith_[A](ta: Try[A])(h: PartialFunction[Throwable, Try[A]]): Try[A] =
    ta.recoverWith(h)

  override def attempt[A](a: => A): Try[A] =
    Try(a)

  override def fromTry[A](ta: Try[A]): Try[A] =
    ta

  override def ensure[A](f: Try[A], e: => Try[Unit]): Try[A] =
    f match
      case Success(x) => Try(e).flatten.map(_ => x)
      case Failure(t) => Try(e).flatten.flatMap(_ => Failure(t))

  override def sequence[A, CC[+A] <: Iterable[A]](xs: CC[Try[A]])(using bf: BuildFrom[CC[Try[A]], A, CC[A]]): Try[CC[A]] =
    val cbf = bf.toFactory(xs)
    xs.partitionMap(_.toEither) match
      case (Nil, rights) => Success[CC[A]](cbf.fromSpecific(rights))
      case (lefts, _)    => Failure[CC[A]](lefts.head)
