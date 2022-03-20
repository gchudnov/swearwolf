package com.github.gchudnov.swearwolf.util.func

import scala.annotation.tailrec
import scala.collection.BuildFrom
import scala.util.control.Exception.*
import scala.util.{ Failure, Success, Try }

given OptionMonad: MonadError[Option] with

  override def succeed[A](a: A): Option[A] =
    Some(a)

  override def map[A, B](ra: Option[A])(f: A => B): Option[B] =
    ra match
      case Some(a) => Some(f(a))
      case _       => ra.asInstanceOf[Option[B]]

  override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
    fa match
      case Some(a) => f(a)
      case _       => fa.asInstanceOf[Option[B]]

  override def fail[A](t: Throwable): Option[A] =
    None

  override protected def handleErrorWith_[A](ra: Option[A])(h: PartialFunction[Throwable, Option[A]]): Option[A] =
    ra match
      case None => h(new RuntimeException("Value is not defined"))
      case _    => ra

  override def attempt[A](a: => A): Option[A] =
    allCatch.opt(a)

  override def ensure[A](f: Option[A], e: => Option[Unit]): Option[A] =
    def cleanup: Option[Unit] =
      Try(e) match
        case Failure(t) => None
        case Success(x) => x

    f match
      case None    => cleanup.flatMap(_ => None)
      case Some(a) => cleanup.map(_ => a)

  override def sequence[A, CC[+A] <: Iterable[A]](xs: CC[Option[A]])(using bf: BuildFrom[CC[Option[A]], A, CC[A]]): Option[CC[A]] =
    val cbf = bf.toFactory(xs)
    xs.partitionMap(_.toRight(new RuntimeException("Value is not defined"))) match
      case (Nil, rights) => Option[CC[A]](cbf.fromSpecific(rights))
      case (lefts, _)    => None

  override def traverse[A, CC[+A] <: Iterable[A], B](xs: CC[A])(f: A => Option[B])(using bf: BuildFrom[CC[A], B, CC[B]]): Option[CC[B]] =
    val builder = bf.newBuilder(xs)
    val i       = xs.iterator
    while i.hasNext do
      f(i.next) match
        case Some(b) => builder += b
        case None    => return None
    Some(builder.result)

  def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
    @tailrec
    def iterate(a: A): Option[B] =
      f(a) match
        case None           => None
        case Some(Left(a1)) => iterate(a1)
        case Some(Right(b)) => Some(b)
    iterate(a)
