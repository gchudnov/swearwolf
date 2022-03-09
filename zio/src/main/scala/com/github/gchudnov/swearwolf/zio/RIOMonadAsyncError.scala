package com.github.gchudnov.swearwolf.zio

import com.github.gchudnov.swearwolf.util.func.MonadAsyncError
import com.github.gchudnov.swearwolf.util.func.Canceler
import zio.{ RIO, UIO, ZIO }

class RIOMonadAsyncError[R] extends MonadAsyncError[RIO[R, *]]:

  override def pure[A](a: A): RIO[R, A] =
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

      Left(UIO(canceler.cancel()))
    }

  override def error[A](t: Throwable): RIO[R, A] =
    RIO.fail(t)

  override protected def handleErrorWith_[A](rt: RIO[R, A])(h: PartialFunction[Throwable, RIO[R, A]]): RIO[R, A] =
    rt.catchSome(h)

  override def eval[A](a: => A): RIO[R, A] =
    RIO.attempt(a)

  override def suspend[A](a: => RIO[R, A]): RIO[R, A] =
    RIO.suspend(a)

  override def flatten[A](ffa: RIO[R, RIO[R, A]]): RIO[R, A] =
    ffa.flatten

  override def ensure[A](f: RIO[R, A], e: => RIO[R, Unit]): RIO[R, A] =
    f.ensuring(e.catchAll(_ => ZIO.unit))


/*
  def collectAll[R, E, A, Collection[+Element] <: Iterable[Element]](
    in: Collection[ZIO[R, E, A]]
  )(implicit
    bf: BuildFrom[Collection[ZIO[R, E, A]], A, Collection[A]],
    trace: ZTraceElement
  ): ZIO[R, E, Collection[A]] =
    foreach(in)(ZIO.identityFn)
*/