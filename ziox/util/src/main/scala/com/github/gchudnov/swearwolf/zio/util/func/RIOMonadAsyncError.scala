package com.github.gchudnov.swearwolf.zio.util.func

import com.github.gchudnov.swearwolf.util.func.{ Canceler, MonadAsyncError }
import zio.{ RIO, UIO, URIO, ZIO }

import scala.annotation.tailrec
import scala.collection.BuildFrom

given RIOMonadAsyncError[R]: MonadAsyncError[RIO[R, *]] with

  override def succeed[A](a: A): RIO[R, A] =
    RIO.succeed(a)

  override def map[A, B](fa: RIO[R, A])(f: A => B): RIO[R, B] =
    fa.map(f)

  override def flatMap[A, B](fa: RIO[R, A])(f: A => RIO[R, B]): RIO[R, B] =
    fa.flatMap(f)

  override def async[A](register: (Either[Throwable, A] => Unit) => Canceler): RIO[R, A] =
    RIO.asyncInterrupt { cb =>
      val canceler = register {
        case Left(t)  => cb(RIO.fail(t))
        case Right(a) => cb(RIO.succeed(a))
      }

      Left(URIO.succeed(canceler.cancel()))
    }

  override def fail[A](t: Throwable): RIO[R, A] =
    RIO.fail(t)

  override protected def handleErrorWith_[A](rt: RIO[R, A])(h: PartialFunction[Throwable, RIO[R, A]]): RIO[R, A] =
    rt.catchSome(h)

  override def attempt[A](a: => A): RIO[R, A] =
    RIO.attempt(a)

  override def suspend[A](a: => RIO[R, A]): RIO[R, A] =
    RIO.suspend(a)

  override def flatten[A](ffa: RIO[R, RIO[R, A]]): RIO[R, A] =
    ffa.flatten

  override def ensure[A](f: RIO[R, A], e: => RIO[R, Unit]): RIO[R, A] =
    f.ensuring(e.ignore)

  override def sequence[A, CC[+A] <: Iterable[A]](xs: CC[RIO[R, A]])(using bf: BuildFrom[CC[RIO[R, A]], A, CC[A]]): RIO[R, CC[A]] =
    ZIO.collectAll(xs)

  override def traverse[A, CC[+A] <: Iterable[A], B](xs: CC[A])(f: A => RIO[R, B])(using bf: BuildFrom[CC[A], B, CC[B]]): RIO[R, CC[B]] =
    ZIO.foreach(xs)(f)

  override def tailRecM[A, B](a: A)(f: A => RIO[R, Either[A, B]]): RIO[R, B] =
    def iterate(a: A): RIO[R, B] =
      f(a).flatMap {
        case Left(a1) => iterate(a1)
        case Right(b) => ZIO.succeed(b)
      }
    ZIO.suspendSucceed(iterate(a))
