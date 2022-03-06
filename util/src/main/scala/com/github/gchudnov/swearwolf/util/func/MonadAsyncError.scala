package com.github.gchudnov.swearwolf.util.func

trait MonadAsyncError[F[_]] extends MonadError[F]:
  def async[A](register: (Either[Throwable, A] => Unit) => Canceler): F[A]


/*
  // Asynchronous Callback-based API
def registerCallback(
    name: String,
    onEvent: Int => Unit,
    onError: Throwable => Unit
): Unit = ???

// Lifting an Asynchronous API to ZStream
val stream = ZStream.async[Any, Throwable, Int] { cb =>
  registerCallback(
    "foo",
    event => cb(ZIO.succeed(Chunk(event))),
    error => cb(ZIO.fail(error).mapError(Some(_)))
  )
}
*/