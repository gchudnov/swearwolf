package com.github.gchudnov.swearwolf.util.func

import scala.collection.BuildFrom
import scala.util.Failure
import scala.util.Success
import scala.util.Try

trait MonadError[F[_]]:
  def succeed[A](a: A): F[A]
  def map[A, B](fa: F[A])(f: A => B): F[B]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def fail[A](e: Throwable): F[A]

  def handleErrorWith[A](fa: => F[A])(h: PartialFunction[Throwable, F[A]]): F[A] =
    Try(fa) match
      case Success(a) =>
        handleErrorWith_(a)(h)
      case Failure(e) if h.isDefinedAt(e) =>
        h(e)
      case Failure(e) =>
        fail(e)

  protected def handleErrorWith_[A](fa: F[A])(f: PartialFunction[Throwable, F[A]]): F[A]

  def attempt[A](a: => A): F[A] =
    map(succeed(()))(_ => a)

  def suspend[A](fa: => F[A]): F[A] =
    flatten(attempt(fa))

  def flatten[A](ffa: F[F[A]]): F[A] =
    flatMap[F[A], A](ffa)(identity)

  def fromTry[A](ta: Try[A]): F[A] =
    ta match
      case Success(x) =>
        succeed(x)
      case Failure(e) =>
        fail(e)

  def fromEither[A](ea: Either[Throwable, A]): F[A] =
    ea match
      case Right(x) =>
        succeed(x)
      case Left(e) =>
        fail(e)

  def ensure[A](f: F[A], e: => F[Unit]): F[A]

  def blocking[A](a: => A): F[A] =
    attempt(a)

  def sequence[A, CC[+A] <: Iterable[A]](xs: CC[F[A]])(using bf: BuildFrom[CC[F[A]], A, CC[A]]): F[CC[A]]

object MonadError:
  def apply[F[_]: MonadError]: MonadError[F] =
    summon[MonadError[F]]

  extension [F[_], A](r: => F[A])
    def map[B](f: A => B)(using ME: MonadError[F]): F[B] =
      ME.map(r)(f)

    def flatMap[B](f: A => F[B])(using ME: MonadError[F]): F[B] =
      ME.flatMap(r)(f)

    def handleErrorWith(h: PartialFunction[Throwable, F[A]])(using ME: MonadError[F]): F[A] =
      ME.handleErrorWith(r)(h)

    def ensure(e: => F[Unit])(using ME: MonadError[F]): F[A] =
      ME.ensure(r, e)

  extension [F[_], A](a: A)
    def succeed(using ME: MonadError[F]): F[A] =
      ME.succeed(a)
