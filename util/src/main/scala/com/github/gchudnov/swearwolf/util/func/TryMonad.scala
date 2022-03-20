package com.github.gchudnov.swearwolf.util.func

import scala.annotation.tailrec
import scala.collection.BuildFrom
import scala.util.{ Failure, Success, Try }

given TryMonad: MonadError[Try] with

  override def succeed[A](a: A): Try[A] =
    Success(a)

  override def map[A, B](ta: Try[A])(f: (A) => B): Try[B] =
    ta.map(f)

  override def flatMap[A, B](ta: Try[A])(f: (A) => Try[B]): Try[B] =
    ta.flatMap(f)

  override def fail[A](t: Throwable): Try[A] =
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

  override def traverse[A, CC[+A] <: Iterable[A], B](xs: CC[A])(f: A => Try[B])(using bf: BuildFrom[CC[A], B, CC[B]]): Try[CC[B]] =
    val builder = bf.newBuilder(xs)
    val i       = xs.iterator
    while i.hasNext do
      f(i.next) match
        case Success(b) => builder += b
        case Failure(e) => return Failure(e)
    Success(builder.result)

  def tailRecM[A, B](a: A)(f: A => Try[Either[A, B]]): Try[B] =
    @tailrec
    def iterate(a: A): Try[B] =
      f(a) match
        case Failure(t)        => Failure(t)
        case Success(Left(a1)) => iterate(a1)
        case Success(Right(b)) => Success(b)
    iterate(a)
