package com.github.gchudnov.swearwolf.term.internal.terminals

import com.github.gchudnov.swearwolf.term.Term
import com.github.gchudnov.swearwolf.util.func.Identity
import com.github.gchudnov.swearwolf.term.keys.KeySeq
import com.github.gchudnov.swearwolf.util.func.FunctionK
import scala.util.control.Exception.allCatch

/**
 * Synchronous Term that throws Exceptions
 */
final class IdTerm(delegate: Term[Identity]) extends Term[Identity]:

  override def read(): Identity[Option[Array[Byte]]] =
    delegate.read()

  override def write(bytes: Array[Byte]): Identity[Unit] =
    delegate.write(bytes)

  override def flush(): Identity[Unit] = 
    delegate.flush()

  override def close(): Identity[Unit] =
    delegate.close()
