package com.github.gchudnov.swearwolf.util.func

import com.github.gchudnov.swearwolf.util.func.Identity

import scala.annotation.tailrec
import scala.collection.BuildFrom

given IdMonad: MonadError[Identity] with

  override def succeed[A](a: A): Identity[A] =
    a

  override def map[A, B](fa: Identity[A])(f: (A) => B): Identity[B] =
    f(fa)

  override def flatMap[A, B](fa: Identity[A])(f: (A) => Identity[B]): Identity[B] =
    f(fa)

  override def fail[A](a: Throwable): Identity[A] =
    throw a

  override protected def handleErrorWith_[A](fa: Identity[A])(h: PartialFunction[Throwable, Identity[A]]): Identity[A] =
    fa

  override def attempt[A](a: => A): Identity[A] =
    a

  override def ensure[A](f: Identity[A], e: => Identity[Unit]): Identity[A] =
    try f
    finally e

  override def sequence[A, CC[+A] <: Iterable[A]](xs: CC[Identity[A]])(using bf: BuildFrom[CC[Identity[A]], A, CC[A]]): Identity[CC[A]] =
    val cbf = bf.toFactory(xs)
    cbf.fromSpecific(xs)

  override def traverse[A, CC[+A] <: Iterable[A], B](xs: CC[A])(f: A => Identity[B])(using bf: BuildFrom[CC[A], B, CC[B]]): Identity[CC[B]] =
    val cbf = bf.toFactory(xs)
    cbf.fromSpecific(xs.map(f))

  def tailRecM[A, B](a: A)(f: A => Identity[Either[A, B]]): Identity[B] =
    @tailrec
    def iterate(a: A): Identity[B] =
      f(a) match
        case Left(a1) => iterate(a1)
        case Right(b) => b
    iterate(a)
