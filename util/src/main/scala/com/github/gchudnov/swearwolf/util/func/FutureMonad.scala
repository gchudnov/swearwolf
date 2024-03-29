package com.github.gchudnov.swearwolf.util.func

import scala.annotation.tailrec
import scala.collection.{ BuildFrom, Factory }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.util.{ Failure, Success, Try }

given FutureMonad(using ec: ExecutionContext): MonadAsyncError[Future] with

  override def succeed[A](a: A): Future[A] =
    Future.successful(a)

  override def map[A, B](fa: Future[A])(f: (A) => B): Future[B] =
    fa.map(f)

  override def flatMap[A, B](fa: Future[A])(f: (A) => Future[B]): Future[B] =
    fa.flatMap(f)

  override def fail[A](t: Throwable): Future[A] =
    Future.failed(t)

  override protected def handleErrorWith_[A](fa: Future[A])(h: PartialFunction[Throwable, Future[A]]): Future[A] =
    fa.recoverWith(h)

  override def attempt[A](a: => A): Future[A] =
    Future(a)

  override def suspend[A](fa: => Future[A]): Future[A] =
    Future(fa).flatMap(identity)

  override def fromTry[A](ta: Try[A]): Future[A] =
    Future.fromTry(ta)

  override def async[A](register: (Either[Throwable, A] => Unit) => Canceler): Future[A] =
    val p = Promise[A]()
    register {
      case Left(a)  => p.failure(a)
      case Right(a) => p.success(a)
    }

    p.future

  override def ensure[A](f: Future[A], e: => Future[Unit]): Future[A] =
    val p = Promise[A]()
    def cleanup =
      Try(e) match
        case Failure(f) => Future.failed(f)
        case Success(v) => v

    f.onComplete {
      case Success(v) => cleanup.map(_ => v).onComplete(p.complete)
      case Failure(f) => cleanup.flatMap(_ => Future.failed(f)).onComplete(p.complete)
    }

    p.future

  override def blocking[A](a: => A): Future[A] =
    Future(scala.concurrent.blocking(a))

  override def sequence[A, CC[+A] <: Iterable[A]](xs: CC[Future[A]])(using bf: BuildFrom[CC[Future[A]], A, CC[A]]): Future[CC[A]] =
    Future.sequence(xs)

  override def traverse[A, CC[+A] <: Iterable[A], B](xs: CC[A])(f: A => Future[B])(using bf: BuildFrom[CC[A], B, CC[B]]): Future[CC[B]] =
    Future.traverse(xs)(f)

  def tailRecM[A, B](a: A)(f: A => Future[Either[A, B]]): Future[B] =
    def iterate(a: A): Future[B] =
      f(a).flatMap {
        case Left(a1) => iterate(a1)
        case Right(b) => Future.successful(b)
      }
    iterate(a)
