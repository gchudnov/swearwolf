package com.github.gchudnov.swearwolf.util.func

object IdMonad extends MonadError[Identity]:

  override def unit[A](a: A): Identity[A] =
    a

  override def map[A, B](fa: Identity[A])(f: (A) => B): Identity[B] =
    f(fa)

  override def flatMap[A, B](fa: Identity[A])(f: (A) => Identity[B]): Identity[B] =
    f(fa)

  override def error[A](a: Throwable): Identity[A] =
    throw a

  override protected def handleErrorWith_[A](fa: Identity[A])(h: PartialFunction[Throwable, Identity[A]]): Identity[A] =
    fa

  override def eval[A](a: => A): Identity[A] =
    a

  override def ensure[A](f: Identity[A], e: => Identity[Unit]): Identity[A] =
    try f
    finally e
