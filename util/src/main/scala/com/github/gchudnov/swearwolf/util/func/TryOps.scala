package com.github.gchudnov.swearwolf.util.func

import scala.util.Try
import com.github.gchudnov.swearwolf.util.func.FunctionK

object TryOps:

  val tryToId: FunctionK[Try, Identity] =
    new FunctionK[Try, Identity]:
      override def apply[A](fa: Try[A]): Identity[A] = fa.get

  val idToTry: FunctionK[Identity, Try] =
    new FunctionK[Identity, Try]:
      override def apply[A](fa: Identity[A]): Try[A] = Try(fa)
