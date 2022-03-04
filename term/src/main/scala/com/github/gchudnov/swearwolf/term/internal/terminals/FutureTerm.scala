package com.github.gchudnov.swearwolf.term.internal.terminals

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.Identity
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import scala.util.Future
import com.github.gchudnov.swearwolf.util.func.FunctionK
import scala.concurrent.Future

final class FutureTerm {

}

/**
 * Asynchronous Terminal.
 */
// final class FutureTerm(delegate: Term[Identity]) extends Term[Future]:

//   override def write(bytes: Array[Byte]): Future[Unit] =
//     Future(delegate.write(bytes))

//   override def flush(): Future[Unit] =
//     Future(delegate.flush())

//   override def poll(): Future[Option[List[KeySeq]]] =
//     Future(delegate.poll())

// TODO: not impl
//   private val futureToId: FunctionK[Future, Identity] =
//     new FunctionK[Future, Identity]:
//       override def apply[A](fa: Future[A]): Identity[A] = fa.get

// TODO: not impl
//   private val idToFuture: FunctionK[Identity, Future] =
//     new FunctionK[Identity, Future]:
//       override def apply[A](fa: Identity[A]): Future[A] = Future(fa)
