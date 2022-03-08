package com.github.gchudnov.swearwolf.util.func

import scala.util.Success
import scala.util.Failure
import scala.util.Try

trait MonadError[F[_]]:
  def pure[A](a: A): F[A]
  def map[A, B](fa: F[A])(f: A => B): F[B]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def error[A](e: Throwable): F[A]

  def handleErrorWith[A](fa: => F[A])(h: PartialFunction[Throwable, F[A]]): F[A] =
    Try(fa) match
      case Success(a)                     => handleErrorWith_(a)(h)
      case Failure(e) if h.isDefinedAt(e) => h(e)
      case Failure(e)                     => error(e)

  protected def handleErrorWith_[A](fa: F[A])(f: PartialFunction[Throwable, F[A]]): F[A]

  def eval[A](a: => A): F[A]         = map(pure(()))(_ => a)
  def suspend[A](fa: => F[A]): F[A]  = flatten(eval(fa))
  def flatten[A](ffa: F[F[A]]): F[A] = flatMap[F[A], A](ffa)(identity)

  def fromTry[A](ta: Try[A]): F[A] =
    ta match
      case Success(x) => pure(x)
      case Failure(e) => error(e)

  def attempt[A](a: => A): F[A] = eval(a)

  def ensure[A](f: F[A], e: => F[Unit]): F[A]

  def blocking[A](a: => A): F[A] = eval(a)

object MonadError:
  def apply[F[_]: MonadError]: MonadError[F] = implicitly[MonadError[F]]

  implicit final class MonadErrorOps[F[_], A](r: => F[A]):
    def map[B](f: A => B)(implicit ME: MonadError[F]): F[B]                                = ME.map(r)(f)
    def flatMap[B](f: A => F[B])(implicit ME: MonadError[F]): F[B]                         = ME.flatMap(r)(f)
    def handleError(h: PartialFunction[Throwable, F[A]])(implicit ME: MonadError[F]): F[A] = ME.handleErrorWith(r)(h)
    def ensure(e: => F[Unit])(implicit ME: MonadError[F]): F[A]                            = ME.ensure(r, e)

  implicit final class MonadErrorValueOps[F[_], A](private val a: A) extends AnyVal:
    def pure(implicit ME: MonadError[F]): F[A] = ME.pure(a)
